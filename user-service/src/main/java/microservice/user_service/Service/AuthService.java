package microservice.user_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.user_service.DTOs.LoginDTO;
import microservice.user_service.DTOs.SignupDTO;
import microservice.user_service.DTOs.UserDTO;

public interface AuthService {
    Result<Void> validateSignupCredentials(SignupDTO signupDTO);
    Result<UserDTO> validateLoginCredentials(LoginDTO loginDTO);
    Result<Void> validateStudent(String accountNumber);
    String getJWTToken(UserDTO userDTO);
    void processLogin(UserDTO userDTO);
}
