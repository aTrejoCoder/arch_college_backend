package microservice.schedule_service.Utils;

import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Teacher.TeacherNameDTO;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupMappingService {

    private final GroupMapper groupMapper;

    public GroupDTO mapGroupToDTOWithTeachers(Group group) {
        GroupDTO groupDTO = groupMapper.entityToDTO(group);

        List<TeacherNameDTO> teachers = group.getTeachers().stream()
                .map(teacher -> new TeacherNameDTO(teacher.getTeacherId() , teacher.composeNameWithTitle()))
                .toList();

        groupDTO.setTeacherDTOS(teachers);

        return groupDTO;
    }
}

