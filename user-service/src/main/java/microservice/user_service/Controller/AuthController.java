package microservice.user_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.user_service.DTOs.LoginDTO;
import microservice.user_service.DTOs.SignupDTO;
import microservice.user_service.DTOs.UserDTO;
import microservice.user_service.Model.Role;
import microservice.user_service.Service.AuthService;
import microservice.user_service.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService,
                          UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/signup/student")
    public ResponseEntity<ResponseWrapper<String>> signupStudent(@Valid @RequestBody SignupDTO signupDTO){
        Result<Void> validationResult = authService.validateSignupCredentials(signupDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(validationResult.getErrorMessage()));
        }

        UserDTO userDTO = userService.createUser(signupDTO, 1L, "student");

        String jwtToken = authService.getJWTToken(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(jwtToken, "User"));
    }

    @PostMapping("/signup/teacher")
    public ResponseEntity<ResponseWrapper<String>> signupTeacher(@Valid @RequestBody SignupDTO signupDTO){
        Result<Void> validationResult = authService.validateSignupCredentials(signupDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(validationResult.getErrorMessage()));
        }

        UserDTO userDTO = userService.createUser(signupDTO, 1L, "teacher");

        String jwtToken = authService.getJWTToken(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(jwtToken, "User"));
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<String>> login(@Valid @RequestBody LoginDTO loginDTO){
        Result<UserDTO> validationResult = authService.validateLoginCredentials(loginDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(validationResult.getErrorMessage()));
        }
        UserDTO userDTO = validationResult.getData();

        authService.processLogin(userDTO);

        String jwtToken = authService.getJWTToken(userDTO);
        return ResponseEntity.ok(ResponseWrapper.ok(jwtToken, "Login successfully completed"));
    }
}
