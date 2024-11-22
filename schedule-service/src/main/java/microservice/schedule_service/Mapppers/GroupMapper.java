package microservice.schedule_service.Mapppers;

import microservice.common_classes.DTOs.Group.ElectiveGroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.OrdinaryGroupInsertDTO;
import microservice.schedule_service.Models.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "obligatorySubjectId", ignore = true)
    @Mapping(target = "subjectName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    Group insertDtoToEntity(OrdinaryGroupInsertDTO ordinaryGroupInsertDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "electiveSubjectId", ignore = true)
    @Mapping(target = "subjectName", ignore = true)
    @Mapping(target = "teachers ", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    Group insertDtoToEntity(ElectiveGroupInsertDTO electiveGroupInsertDTO);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "schoolPeriod", ignore = true)
    void updateDto(@MappingTarget Group group,  OrdinaryGroupInsertDTO ordinaryGroupInsertDTO);

    GroupDTO entityToDTO(Group group);


}
