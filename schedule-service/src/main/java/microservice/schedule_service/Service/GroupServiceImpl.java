package microservice.schedule_service.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.DTOs.Teacher.TeacherNameDTO;
import microservice.schedule_service.Mapppers.TeacherMapper;
import microservice.schedule_service.Models.GroupRelationshipsDTO;
import microservice.schedule_service.Models.Teacher;
import microservice.common_classes.Utils.GroupStatus;
import microservice.common_classes.Utils.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ExternalRelationsService relationshipService;
    private final ScheduleService scheduleService;
    private final KeyGenerationService keyGenerationService;
    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;

    private final String currentSemester = SemesterData.getCurrentSemester();


    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            GroupMapper groupMapper,
                            ExternalRelationsService relationshipService,
                            ScheduleService scheduleService,
                            KeyGenerationService keyGenerationService,
                            TeacherMapper teacherMapper, TeacherRepository teacherRepository) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.relationshipService = relationshipService;
        this.scheduleService = scheduleService;
        this.keyGenerationService = keyGenerationService;
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;
    }


    @Override
    @Cacheable(value = "groupById", key = "#groupId")
    public Result<GroupDTO> getGroupById(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        return optionalGroup.map(group -> Result.success(mapEntityToDTO(group)))
                .orElseGet(() -> Result.error("Group with Id " + groupId + " not found"));
    }

    @Override
    @Cacheable(value = "groupCurrentByKey", key = "#key")
    public Result<GroupDTO> getGroupCurrentByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester);
        return optionalGroup.map(group -> Result.success(mapEntityToDTO(group)))
                .orElseGet(() -> Result.error("Group with Key " + key + " not found"));
    }

    @Override
    public List<GroupDTO> getCurrentGroupsByClassroom(String classroom) {
        return null;
    }

    @Override
    public List<GroupDTO> getCurrentGroupsByClassroomPrefix(char buildingLetter) {
        return List.of();
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
    public GroupDTO createGroup(OrdinaryGroupInsertDTO ordinaryGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(ordinaryGroupInsertDTO);
        group.setSchoolPeriod(currentSemester);
        group.setSchedule(scheduleService.mapScheduleDTOToEntity(ordinaryGroupInsertDTO.getSchedule()));
        group.setKey(keyGenerationService.generateOrdinaryKey(group,groupRelationshipsDTO.getOrdinarySubjectDTO()));

        handleExternalGroupRelationships(group, groupRelationshipsDTO);

        groupRepository.saveAndFlush(group);

        return mapEntityToDTO(group);
    }

    @Override
    @Transactional
    public GroupDTO createGroup(ElectiveGroupInsertDTO electiveGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(electiveGroupInsertDTO);
        group.setSchedule(scheduleService.mapScheduleDTOToEntity(electiveGroupInsertDTO.getSchedule()));
        group.setSchoolPeriod(currentSemester);

        if (groupRelationshipsDTO.getOrdinarySubjectDTO() != null) {
            group.setKey(keyGenerationService.generateOrdinaryKey(group,groupRelationshipsDTO.getOrdinarySubjectDTO()));
        }

        if (groupRelationshipsDTO.getElectiveSubjectDTO() != null) {
            group.setKey(keyGenerationService.generateElectiveKey(group,groupRelationshipsDTO.getElectiveSubjectDTO()));
        }

        handleExternalGroupRelationships(group, groupRelationshipsDTO);

        groupRepository.saveAndFlush(group);

        return mapEntityToDTO(group);
    }

    @Override
    public GroupDTO updateGroupSchedule(GroupScheduleUpdateDTO groupUpdateDTO) {
        Group group = groupRepository.findById(groupUpdateDTO.getGroup_id()).orElseThrow(() -> new EntityNotFoundException("Group with Id " + groupUpdateDTO.getGroup_id() + " not found"));
        group.setClassroom(groupUpdateDTO.getClassroom());
        group.setSchedule(scheduleService.mapScheduleDTOToEntity(groupUpdateDTO.getSchedule()));


        groupRepository.saveAndFlush(group);
        return mapEntityToDTO(group);
    }

    @Override
    public GroupDTO updateGroupTeacher(GroupScheduleUpdateDTO groupUpdateDTO) {
        return null;
    }

    @Override
    public GroupDTO updateGroupStatus(GroupStatus groupStatus, String groupKey) {
        Group group = groupRepository.findByKeyAndSchoolPeriod(groupKey, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + groupKey + " not found"));

        group.setGroupStatus(groupStatus);

        groupRepository.saveAndFlush(group);
        return mapEntityToDTO(group);
    }

    @Override
    public GroupDTO addSpots(String key, int spotsToAdd) {
        Group group = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + key + " not found"));

        group.increaseSpots(spotsToAdd);

        groupRepository.saveAndFlush(group);

        return mapEntityToDTO(group);
    }

    @Override
    public GroupDTO removeTeacher(String groupKey, Long teacherId) {
        Group group = groupRepository.findByKeyAndSchoolPeriod(groupKey, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + groupKey + " not found"));

        Optional<Teacher> optionalTeacher = group.getTeachers().stream().filter(teacher ->  teacher.getTeacherId().equals(teacherId)).findAny();
        if (optionalTeacher.isEmpty()) {
            throw  new EntityNotFoundException("Teacher Not Found in group");
        }

        group.removeTeacher(optionalTeacher.get());

        groupRepository.saveAndFlush(group);

        return mapEntityToDTO(group);
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
        relationshipService.addRelationshipSubject(group, groupRelationshipsDTO);

        List<Teacher> teachers = groupRelationshipsDTO.getTeacherDTOS().stream()
                .map(teacherDTO -> teacherRepository.findById(teacherDTO.getTeacherId())
                        .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherDTO.getTeacherId())))
                .toList();

        group.addTeachers(teachers);
    }


    public Result<Void> validateSpotIncrease(int spotsToAdd) {
        if (spotsToAdd > 10) {
            return Result.error("spots can't increase can't be above 10 ");
        } else {
            return Result.success();
        }
    }


    private GroupDTO mapEntityToDTO(Group group) {
        GroupDTO groupDTO = groupMapper.entityToDTO(group);

        List<TeacherNameDTO> teachers = teacherMapper.teachersToDTOs(group.getTeachers());
        groupDTO.setTeacherDTOS(teachers);

        return groupDTO;
    }


}

