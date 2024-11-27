package microservice.schedule_service.Service.GroupServices;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Models.Teacher;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Utils.GroupMappingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupDeleteServiceImpl {

    private final GroupRepository groupRepository;
    private final GroupMappingService mappingService;
    private final String currentSemester = SemesterData.getCurrentSchoolPeriod();


    public GroupDTO deleteTeacher(String groupKey, Long teacherId) {
        Group group = groupRepository.findByKeyAndSchoolPeriod(groupKey, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + groupKey + " not found"));

        Optional<Teacher> optionalTeacher = group.getTeachers().stream().filter(teacher ->  teacher.getTeacherId().equals(teacherId)).findAny();
        if (optionalTeacher.isEmpty()) {
            throw  new EntityNotFoundException("Teacher Not Found in group");
        }

        group.removeTeacher(optionalTeacher.get());

        groupRepository.saveAndFlush(group);

        return mappingService.mapGroupToDTOWithTeachers(group);
    }

    public void deleteCurrentGroupByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester);
        if (optionalGroup.isEmpty()) {
            throw new EntityNotFoundException("Group with Key " + key + " not found");
        }

        groupRepository.delete(optionalGroup.get());
    }
}

