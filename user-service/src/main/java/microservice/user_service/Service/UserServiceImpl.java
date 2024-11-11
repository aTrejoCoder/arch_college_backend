package microservice.user_service.Service;

import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import microservice.user_service.DTOs.SignupDTO;
import microservice.user_service.DTOs.UserDTO;
import microservice.user_service.Mappers.UserMapper;
import microservice.user_service.Middleware.PasswordUtil;
import microservice.user_service.Model.Role;
import microservice.user_service.Model.User;
import microservice.user_service.Repository.RoleRepository;
import microservice.user_service.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           UserRepository userRepository,
                           RoleRepository roleRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDTO createUser(SignupDTO signupDTO, Long studentId, String roleName) {
        String hashedPassword = PasswordUtil.hashPassword(signupDTO.getPassword());

        User user = User.builder()
                .email(signupDTO.getEmail())
                .phoneNumber(signupDTO.getPhoneNumber())
                .username(signupDTO.getAccountNumber())
                .password(hashedPassword)
                .joinedAt(LocalDateTime.now())
                .studentId(studentId)
                .lastLogin(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.saveAndFlush(user);

        addRole(user, roleName);

        return userMapper.entityToDTO(user);
    }

    @Override
    @Cacheable(value = "userById", key = "#userId")
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return userMapper.entityToDTO(user);
    }

    private void addRole(User user, String roleName) {
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if (optionalRole.isEmpty()) {
            Role newRole = new Role(roleName);
            roleRepository.saveAndFlush(newRole);

            user.getRoles().add(newRole);
            userRepository.save(user);
        } else {
            user.getRoles().add(optionalRole.get());
            userRepository.save(user);
        }
    }
}
