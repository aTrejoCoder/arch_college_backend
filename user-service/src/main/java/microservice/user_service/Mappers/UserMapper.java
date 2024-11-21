package microservice.user_service.Mappers;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.User.ProfileDTO;
import microservice.common_classes.DTOs.User.UserDTO;
import microservice.user_service.Model.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDTO entityToDTO(User user);

    ProfileDTO entityToProfileDTO(User user);

    void studentDTOToProfileDTO(@MappingTarget ProfileDTO profileDTO, StudentDTO studentDTO);

}
