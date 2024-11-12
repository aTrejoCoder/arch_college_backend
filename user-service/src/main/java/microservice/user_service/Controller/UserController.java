package microservice.user_service.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.User.ProfileDTO;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.user_service.Middleware.JWTSecurity;
import microservice.user_service.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/api/user")
public class UserController {
    private final JWTSecurity jwtSecurity;
    private final UserService userService;

    public UserController(JWTSecurity jwtSecurity, UserService userService) {
        this.jwtSecurity = jwtSecurity;
        this.userService = userService;
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ResponseWrapper<ProfileDTO>> getMyProfile(HttpServletRequest request) {
        Result<String> clientIdResult = jwtSecurity.getUsernameFromToken(request);
        if (!clientIdResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseWrapper.error(clientIdResult.getErrorMessage(), 500));
        }

        ProfileDTO profileDTO = userService.getProfileDataByUsername(clientIdResult.getData());

        return ResponseEntity.ok(ResponseWrapper.found(profileDTO, "User Profile"));
    }
}
