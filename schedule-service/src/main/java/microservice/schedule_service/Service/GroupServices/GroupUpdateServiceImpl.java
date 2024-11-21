package microservice.schedule_service.Service.GroupServices;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.GroupScheduleUpdateDTO;
import microservice.common_classes.Utils.Group.GroupStatus;
import microservice.common_classes.Utils.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.Teacher;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Repository.TeacherRepository;
import microservice.schedule_service.Service.ScheduleService;
import microservice.schedule_service.Utils.GroupMappingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupUpdateServiceImpl {

    private final GroupRepository groupRepository;
    private final GroupQueryService groupQueryService;
    private final TeacherRepository teacherRepository;
    private final ScheduleService scheduleService;
    private final GroupValidationService groupValidationService;
    private final GroupMappingService mappingService;
    private final GroupValidationService validationService;
    private final String currentSemester = SemesterData.getCurrentSemester();


    public GroupDTO updateGroupSchedule(GroupScheduleUpdateDTO groupUpdateDTO) {
        Group group = groupRepository.findById(groupUpdateDTO.getGroup_id()).orElseThrow(() -> new EntityNotFoundException("Group with Id " + groupUpdateDTO.getGroup_id() + " not found"));
        group.setClassroom(groupUpdateDTO.getClassroom());
        group.setSchedule(scheduleService.mapScheduleDTOToEntity(groupUpdateDTO.getSchedule()));


        groupRepository.saveAndFlush(group);
        return mappingService.mapGroupToDTOWithTeachers(group);
    }

    public Result<Void> cancelGroup(String groupKey) {
        return validationService.cancelGroupIfEligible(groupKey, currentSemester);
    }

    @Transactional
    public GroupDTO removeTeacherToGroup(String groupKey, Long teacherId) {
        Group group = groupRepository.findByKeyAndSchoolPeriod(groupKey, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + groupKey + " not found"));

        Optional<Teacher> optionalTeacher = group.getTeachers()
                .stream()
                .filter(teacher -> teacher.getTeacherId().equals(teacherId))
                .findAny();

        if (optionalTeacher.isEmpty()) {
           throw new EntityNotFoundException("Teacher with ID " + groupKey + " not found in requested group");
        }

        group.removeTeacher(optionalTeacher.get());

        return mappingService.mapGroupToDTOWithTeachers(group);

    }


    @Transactional
    public Result<GroupDTO> addTeacherToGroup(String groupKey, Long teacherId) {
        Group group = groupQueryService.findGroupByKey(groupKey)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + groupKey + " not found"));

        Teacher newTeacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + teacherId + " not found"));

        Result<Void> teacherResult = groupValidationService.validateNotDuplicatedTeacherInGroup(group, newTeacher);
        if (!teacherResult.isSuccess()) {
            return Result.error(teacherResult.getErrorMessage());
        }

        group.addTeacher(newTeacher);

        groupRepository.saveAndFlush(group);

        GroupDTO groupDTO = mappingService.mapGroupToDTOWithTeachers(group);
        return  Result.success(groupDTO);

    }
}
