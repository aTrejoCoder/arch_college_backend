package microservice.schedule_service.Service;

import jakarta.persistence.EntityNotFoundException;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.DTO.*;
import microservice.schedule_service.Models.Schedule;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Utils.SemesterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;


@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final ScheduleValidationService scheduleValidationService;
    private final KeyGenerationService keyGenerationService;
    private final GroupMapper groupMapper;
    private final String currentSemester = SemesterData.getCurrentSemester();

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            ScheduleValidationService scheduleValidationService,
                            KeyGenerationService keyGenerationService,
                            GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.scheduleValidationService = scheduleValidationService;
        this.keyGenerationService = keyGenerationService;
        this.groupMapper = groupMapper;
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
        Optional<Group> optionalGroup = groupRepository.findByKeyAndSemester(key, currentSemester);
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
        List<Group> groups = groupRepository.findByTeacherIdAndSemester(subjectId, currentSemester);
        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    public void createGroup(GroupInsertDTO groupInsertDTO) {
        Group group = groupMapper.insertDtoToEntity(groupInsertDTO);
        group.setCurrentSemester();
        group.setKey(keyGenerationService.generateKey(group));

        groupRepository.save(group);
    }

    // TO BE FIXED
    @Override
    public void updateGroup(GroupUpdateDTO groupUpdateDTO) {
        Group group = groupMapper.updateDtoToEntity(groupUpdateDTO);
        groupRepository.save(group);
    }

    @Override
    public void deleteCurrentGroupByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKeyAndSemester(key, currentSemester);
        if (optionalGroup.isEmpty()) {
            throw new EntityNotFoundException("Group with Key " + key + " not found");
        }

        groupRepository.delete(optionalGroup.get());
    }


    @Override
    public Result<Void> validateGroupSchedule(String classroom, List<ScheduleInsertDTO> schedules) {
        return scheduleValidationService.validateSchedule(classroom, schedules, SemesterData.getCurrentSemester());
    }

}

