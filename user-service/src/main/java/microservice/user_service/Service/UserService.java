package microservice.user_service.Service;

import microservice.common_classes.DTOs.User.ProfileDTO;
import microservice.common_classes.DTOs.User.SignupDTO;
import microservice.common_classes.DTOs.User.UserDTO;


public interface UserService {
    UserDTO createUser(SignupDTO signupDTO, String roleName);
    UserDTO getUserById(Long userId);
    void addMemberRelationAsync(String username);
    ProfileDTO getProfileDataByUsername(String username);
}
