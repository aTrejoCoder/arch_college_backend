package microservice.user_service.Mappers;

import microservice.user_service.DTOs.UserDTO;
import microservice.user_service.Model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO entityToDTO(User student);
}
