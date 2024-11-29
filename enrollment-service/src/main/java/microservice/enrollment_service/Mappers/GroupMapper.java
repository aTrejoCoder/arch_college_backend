package microservice.enrollment_service.Mappers;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.enrollment_service.Model.Preload.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    Group dtoToEntity(GroupDTO groupDTO);
}
