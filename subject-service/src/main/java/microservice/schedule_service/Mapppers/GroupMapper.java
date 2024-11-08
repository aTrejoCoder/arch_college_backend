package microservice.schedule_service.Mapppers;

import microservice.schedule_service.DTO.GroupDTO;
import microservice.schedule_service.DTO.GroupInsertDTO;
import microservice.schedule_service.DTO.GroupUpdateDTO;
import microservice.schedule_service.Models.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Group insertDtoToEntity(GroupInsertDTO groupInsertDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Group updateDtoToEntity(GroupUpdateDTO groupUpdateDTO);


    GroupDTO entityToDTO(Group group);
}
