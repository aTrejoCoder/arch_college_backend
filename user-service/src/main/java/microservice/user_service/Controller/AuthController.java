package microservice.user_service.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.User.LoginDTO;
import microservice.common_classes.DTOs.User.SignupDTO;
import microservice.common_classes.DTOs.User.UserDTO;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.user_service.Service.AuthService;
import microservice.user_service.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
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
        Result<Void> validateStudentResult = authService.validateStudent(signupDTO.getAccountNumber());
        if (!validateStudentResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(validateStudentResult.getErrorMessage()));
        }

        Result<Void> passwordFormatResult = authService.validatePasswordFormat(signupDTO.getPassword());
        if (!passwordFormatResult.isSuccess()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(passwordFormatResult.getErrorMessage()));
        }

        Result<Void> credentialsResult = authService.validateSignupCredentials(signupDTO);
        if (!credentialsResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(credentialsResult.getErrorMessage()));
        }

        UserDTO userDTO = userService.createUser(signupDTO, "student");

        userService.addMemberRelationAsync(userDTO.getUsername());

        String jwtToken = authService.getJWTToken(userDTO);
        log.info("signupStudent -> student with accountNumber {} successfully singed up", signupDTO.getAccountNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(jwtToken, "User"));
    }


    @PostMapping("/signup/teacher")
    public ResponseEntity<ResponseWrapper<String>> signupTeacher(@Valid @RequestBody SignupDTO signupDTO){
        Result<Void> validateStudentResult = authService.validateTeacher(signupDTO.getAccountNumber());
        if (!validateStudentResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(validateStudentResult.getErrorMessage()));
        }

        Result<Void> passwordFormatResult = authService.validatePasswordFormat(signupDTO.getPassword());
        if (!passwordFormatResult.isSuccess()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(passwordFormatResult.getErrorMessage()));
        }

        Result<Void> validationResult = authService.validateSignupCredentials(signupDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(validationResult.getErrorMessage()));
        }

        UserDTO userDTO = userService.createUser(signupDTO, "teacher");
        userService.addMemberRelationAsync(userDTO.getUsername());

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
