package microservice.schedule_service.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.Utils.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ExternalRelationshipService relationshipService;
    private final ScheduleService scheduleService;
    private final KeyGenerationService keyGenerationService;

    private final String currentSemester = SemesterData.getCurrentSemester();


    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            GroupMapper groupMapper,
                            ExternalRelationshipService relationshipService,
                            ScheduleService scheduleService,
                            KeyGenerationService keyGenerationService) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.relationshipService = relationshipService;
        this.scheduleService = scheduleService;
        this.keyGenerationService = keyGenerationService;
    }


    @Override
    @Cacheable(value = "groupById", key = "#groupId")
    public Result<GroupDTO> getGroupById(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        return optionalGroup.map(group -> Result.success(groupMapper.entityToDTO(group)))
                .orElseGet(() -> Result.error("Group with Id " + groupId + " not found"));
    }

    @Override
    @Cacheable(value = "groupCurrentByKey", key = "#key")
    public Result<GroupDTO> getGroupCurrentByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester);
        return optionalGroup.map(group -> Result.success(groupMapper.entityToDTO(group)))
                .orElseGet(() -> Result.error("Group with Key " + key + " not found"));
    }

    @Override
    @Cacheable(value = "currentGroupsBySubjectId", key = "#subjectId")
    public List<GroupDTO> getCurrentGroupsBySubjectId(Long subjectId) {
        List<Group> groups = groupRepository.findByOrdinarySubjectId(subjectId);

        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    @Cacheable(value = "currentGroupsByTeacherId", key = "#subjectId")
    public List<GroupDTO> getCurrentGroupsByTeacherId(Long subjectId) {
        List<Group> groups = groupRepository.findByTeacherIdAndSchoolPeriod(subjectId, currentSemester);
        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    @Transactional
    public GroupDTO createGroup(GroupInsertDTO groupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(groupInsertDTO);
        group.setSchoolPeriod(currentSemester);
        group.setSchedule(scheduleService.mapScheduleDTOToEntity(groupInsertDTO.getSchedule()));

        if (groupRelationshipsDTO.getOrdinarySubjectDTO() != null) {
            group.setKey(keyGenerationService.generateOrdinaryKey(group,groupRelationshipsDTO.getOrdinarySubjectDTO()));
        }

        if (groupRelationshipsDTO.getElectiveSubjectDTO() != null) {
            group.setKey(keyGenerationService.generateElectiveKey(group,groupRelationshipsDTO.getElectiveSubjectDTO()));
        }

        handleExternalGroupRelationships(group, groupRelationshipsDTO);

        groupRepository.saveAndFlush(group);

        return groupMapper.entityToDTO(group);
    }

    @Override
    public GroupDTO updateGroup(GroupUpdateDTO groupUpdateDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupRepository.findById(groupUpdateDTO.getGroup_id()).orElseThrow(() -> new EntityNotFoundException("Group with Id " + groupUpdateDTO.getGroup_id() + " not found"));

        handleExternalGroupRelationships(group, groupRelationshipsDTO);

        if (groupUpdateDTO.isRemoveTeacher()) {
            group.clearTeacher();
        }

        groupRepository.saveAndFlush(group);

        return groupMapper.entityToDTO(group);
    }

    @Override
    public void deleteCurrentGroupByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester);
        if (optionalGroup.isEmpty()) {
            throw new EntityNotFoundException("Group with Key " + key + " not found");
        }

        groupRepository.delete(optionalGroup.get());
    }

    private void handleExternalGroupRelationships(Group group, GroupRelationshipsDTO groupRelationshipsDTO){
        if (groupRelationshipsDTO.getTeacherDTO() != null) {
            relationshipService.addTeacher(group, groupRelationshipsDTO);
        }

        if (groupRelationshipsDTO.getElectiveSubjectDTO() != null || groupRelationshipsDTO.getOrdinarySubjectDTO() == null) {
            relationshipService.addRelationshipSubject(group, groupRelationshipsDTO);
        }

    }
}

