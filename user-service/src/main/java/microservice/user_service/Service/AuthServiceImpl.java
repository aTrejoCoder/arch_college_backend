package microservice.user_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.user_service.DTOs.LoginDTO;
import microservice.user_service.DTOs.SignupDTO;
import microservice.user_service.DTOs.UserDTO;
import microservice.user_service.Mappers.UserMapper;
import microservice.user_service.Middleware.JWTSecurity;
import microservice.user_service.Model.Role;
import microservice.user_service.Model.User;
import microservice.user_service.Repository.UserRepository;
import microservice.user_service.Middleware.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements  AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JWTSecurity jwtSecurity;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           JWTSecurity jwtSecurity) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtSecurity = jwtSecurity;
    }

    @Override
    public Result<Void> validateSignupCredentials(SignupDTO signupDTO) {
        Optional<User> optionalEmailUser = userRepository.findByEmail(signupDTO.getEmail());
        if (optionalEmailUser.isPresent()) {
            return Result.error("email already taken");
        }

        if (signupDTO.getPhoneNumber() != null) {
            Optional<User> optionalPhoneUser = userRepository.findByPhoneNumber(signupDTO.getPhoneNumber());
            if (optionalPhoneUser.isPresent()) {
                return Result.error("phone_number already taken");
            }
        }

        return Result.success();
    }

    @Override
    @Cacheable(value = "loginCache", key = "#loginDTO.accountNumber")
    public Result<UserDTO> validateLoginCredentials(LoginDTO loginDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(loginDTO.getAccountNumber());
        if (optionalUser.isEmpty()) {
            return Result.error("invalid account number");
        }

        User user = optionalUser.get();

        boolean isPasswordCorrect = PasswordUtil.validatePassword(loginDTO.getPassword(), user.getPassword());
        if (!isPasswordCorrect) {
            return Result.error("Wrong Password");
        }

        return Result.success(userMapper.entityToDTO(user));
    }

    @Override
    public Result<Void> validateStudent(String accountNumber) {
            // Validate on Student-service

            Optional<User> optionalUser = userRepository.findByUsername(accountNumber);
            if(optionalUser.isPresent()) {
                return  Result.error("Student already has an account");
            }

        return Result.success();
    }

    @Override
    @Async("taskExecutor")
    public void processLogin(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        User user = optionalUser.get();

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

    }

    @Override
    public String getJWTToken(UserDTO userDTO) {
        List<String> roleName = userDTO.getRoles().stream().map(Role::getName).toList();
        return jwtSecurity.generateToken(userDTO.getId(), userDTO.getUsername(), roleName);
    }
}
