package microservice.user_service.Service;

import microservice.common_classes.DTOs.User.LoginDTO;
import microservice.common_classes.DTOs.User.RoleDTO;
import microservice.common_classes.DTOs.User.SignupDTO;
import microservice.common_classes.DTOs.User.UserDTO;
import microservice.common_classes.FacadeService.Student.StudentFacadeService;
import microservice.common_classes.FacadeService.Teacher.TeacherFacadeService;
import microservice.common_classes.Utils.Result;
import microservice.user_service.Mappers.UserMapper;
import microservice.user_service.Middleware.JWTSecurity;
import microservice.user_service.Model.User;
import microservice.user_service.Repository.UserRepository;
import microservice.user_service.Middleware.PasswordUtil;
import microservice.user_service.Utils.AccountNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthServiceImpl implements  AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JWTSecurity jwtSecurity;
    private final StudentFacadeService studentFacadeService;
    private final TeacherFacadeService teacherFacadeService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           JWTSecurity jwtSecurity,
                           @Qualifier("studentFacadeServiceImpl") StudentFacadeService studentFacadeService,
                           @Qualifier("teacherFacadeServiceImpl") TeacherFacadeService teacherFacadeService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtSecurity = jwtSecurity;
        this.studentFacadeService = studentFacadeService;
        this.teacherFacadeService = teacherFacadeService;
    }

    @Override
    public Result<Void> validateSignupCredentials(SignupDTO signupDTO) {
        Result<Void> emailResult = validateUniqueEmail(signupDTO.getEmail());
        if (!emailResult.isSuccess()) {
            return Result.error(emailResult.getErrorMessage());
        }

        if (signupDTO.getPhoneNumber() != null) {
            Result<Void> phoneNumberResult = validateUniquePhoneNumber(signupDTO.getPhoneNumber());
            if (!phoneNumberResult.isSuccess()) {
                return Result.error(phoneNumberResult.getErrorMessage());
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
    public Result<Void> validateExistingMember(String accountNumber) {
        boolean isStudentFormat = AccountNumberValidator.isStudentAccountNumber(accountNumber);
        if (!isStudentFormat) {
            return validateStudent(accountNumber);
        }
        else {
            return validateTeacher(accountNumber);
        }
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
        List<String> roleName = userDTO.getRoles().stream().map(RoleDTO::getName).toList();
        return jwtSecurity.generateToken(userDTO.getId(), userDTO.getUsername(), roleName);
    }

    @Override
    public Result<Void> validatePasswordFormat(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (password == null || !password.matches(passwordPattern)) {
            return Result.error("Password must be at least 8 characters long, contain one uppercase letter, one lowercase letter, one digit, and one special character(@,$,!,%,*,?,&).");
        }

        return Result.success();
    }



    public Result<Void> validateTeacher(String accountNumber) {
        CompletableFuture<Boolean> exisitingTeacherFuture = teacherFacadeService.validateExisitingTeacher(accountNumber);
        CompletableFuture<Optional<User>> optionalUserFuture = CompletableFuture.supplyAsync(() -> userRepository.findByUsername(accountNumber)
        );

        return CompletableFuture.allOf(exisitingTeacherFuture, optionalUserFuture)
                .thenApply(v -> {
                    Boolean isTeacherExisting = exisitingTeacherFuture.join();
                    if (!isTeacherExisting) {
                        return Result.<Void>error("Invalid account number");
                    }

                    Optional<User> optionalUser = optionalUserFuture.join();
                    if (optionalUser.isPresent()) {
                        return Result.<Void>error("Teacher already has an account");
                    }

                    return Result.success();
                }).join();
    }

    @Override
    public Result<Void> validateStudent(String accountNumber) {
        CompletableFuture<Boolean> exisitingStudentFuture = studentFacadeService.validateExisitingStudent(accountNumber);
        CompletableFuture<Optional<User>> optionalUserFuture = CompletableFuture.supplyAsync(() -> userRepository.findByUsername(accountNumber)
        );

        return CompletableFuture.allOf(exisitingStudentFuture, optionalUserFuture)
                .thenApply(v -> {
                    Boolean isStudentExisting = exisitingStudentFuture.join();
                    if (!isStudentExisting) {
                        return Result.<Void>error("Invalid account number");
                    }

                    Optional<User> optionalUser = optionalUserFuture.join();
                    if (optionalUser.isPresent()) {
                        return Result.<Void>error("Teacher already has an account");
                    }

                    return Result.success();
                }).join();
    }

    private Result<Void> validateUniquePhoneNumber(String phoneNumber) {
        Optional<User> optionalPhoneUser = userRepository.findByPhoneNumber(phoneNumber);

        if (optionalPhoneUser.isPresent()) {
            return Result.error("phone_number already taken");
        }
          return Result.success();
    }

    private Result<Void> validateUniqueEmail(String email) {
        Optional<User> optionalEmailUser = userRepository.findByEmail(email);
        if (optionalEmailUser.isPresent()) {
            return Result.error("email already taken");
        }

        return Result.success();
    }

}
