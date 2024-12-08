package microservice.user_service.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.User.ProfileDTO;
import microservice.common_classes.JWT.JWTSecurity;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.user_service.Service.UserService;
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
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        ProfileDTO profileDTO = userService.getProfileDataByUsername(accountNumber);

        return ResponseEntity.ok(ResponseWrapper.found(profileDTO, "User Profile"));
    }
}
