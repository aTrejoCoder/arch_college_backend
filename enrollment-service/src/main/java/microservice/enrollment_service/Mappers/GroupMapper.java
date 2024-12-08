package microservice.enrollment_service.Mappers;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.enrollment_service.Model.Preload.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "headTeacherAccountNumber", source = "groupDTO.headTeacherAccountNumber")
    Group dtoToEntity(GroupDTO groupDTO);

}
