package microservice.user_service.Service;

import microservice.common_classes.DTOs.User.LoginDTO;
import microservice.common_classes.DTOs.User.SignupDTO;
import microservice.common_classes.DTOs.User.UserDTO;
import microservice.common_classes.Utils.Result;


public interface AuthService {
    Result<Void> validateSignupCredentials(SignupDTO signupDTO);
    Result<UserDTO> validateLoginCredentials(LoginDTO loginDTO);
    Result<Void> validateExistingMember(String accountNumber);
    String getJWTToken(UserDTO userDTO);
    void processLogin(UserDTO userDTO);
    Result<Void> validatePasswordFormat(String password);
    Result<Void> validateTeacher(String accountNumber);
    Result<Void> validateStudent(String accountNumber);
}
