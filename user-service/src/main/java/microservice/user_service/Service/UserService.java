package microservice.user_service.Service;

import microservice.user_service.DTOs.SignupDTO;
import microservice.user_service.DTOs.UserDTO;
import microservice.user_service.Model.Role;

import java.util.List;

public interface UserService {
    UserDTO createUser(SignupDTO signupDTO, Long studentId, String roleName);
    UserDTO getUserById(Long userId);

}
