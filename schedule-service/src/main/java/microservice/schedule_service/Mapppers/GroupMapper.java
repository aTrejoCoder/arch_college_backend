package microservice.schedule_service.Mapppers;

import microservice.common_classes.DTOs.Group.ElectiveGroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.ObligatoryGroupInsertDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.schedule_service.Models.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subjectId", ignore = true)
    @Mapping(target = "subjectName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    Group insertDtoToEntity(ObligatoryGroupInsertDTO obligatoryGroupInsertDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subjectId", ignore = true)
    @Mapping(target = "subjectName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    Group insertDtoToEntity(ElectiveGroupInsertDTO electiveGroupInsertDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "groupKey", ignore = true)
    @Mapping(target = "schoolPeriod", ignore = true)
    void updateDto(@MappingTarget Group group,  ObligatoryGroupInsertDTO OBligatoryGroupInsertDTO);

    GroupDTO entityToDTO(Group group);


}
