package microservice.schedule_service.Mapppers;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.GroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupNamedDTO;
import microservice.common_classes.DTOs.Group.GroupUpdateDTO;
import microservice.schedule_service.Models.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordinarySubjectId", ignore = true)
    @Mapping(target = "electiveSubjectId", ignore = true)
    @Mapping(target = "subjectName", ignore = true)
    @Mapping(target = "teacherId", ignore = true)
    @Mapping(target = "teacherName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Group insertDtoToEntity(GroupInsertDTO groupInsertDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Group updateDtoToEntity(GroupUpdateDTO groupUpdateDTO);


    GroupDTO entityToDTO(Group group);
    GroupNamedDTO entityToNamedDTO(Group group);
}
