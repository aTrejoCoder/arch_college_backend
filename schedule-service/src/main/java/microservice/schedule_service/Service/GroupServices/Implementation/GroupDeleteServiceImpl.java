package microservice.schedule_service.Service.GroupServices.Implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Teacher;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Models.Group;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupDeleteServiceImpl {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final String currentSemester = AcademicData.getCurrentSchoolPeriod();

    public GroupDTO deleteTeacher(String groupKey, Long teacherId) {
        Group group = groupRepository.findByGroupKeyAndSchoolPeriod(groupKey, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + groupKey + " not found"));

        Optional<Teacher> optionalTeacher = group.getTeachers().stream().filter(teacher -> teacher.getTeacherId().equals(teacherId)).findAny();
        if (optionalTeacher.isEmpty()) {
            throw new EntityNotFoundException("Teacher Not Found in group");
        }

        group.removeTeacher(optionalTeacher.get());

        groupRepository.saveAndFlush(group);
        log.info("Teacher with ID {} removed from group with key {}", teacherId, groupKey);
        log.info("Group with key {} updated ", groupKey);

        return groupMapper.entityToDTO(group);
    }

    public void deleteCurrentGroupByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByGroupKeyAndSchoolPeriod(key, currentSemester);
        if (optionalGroup.isEmpty()) {
            throw new EntityNotFoundException("Group with Key " + key + " not found");
        }

        groupRepository.delete(optionalGroup.get());
        log.info("Group with key {} deleted from the database", key);
    }
}
