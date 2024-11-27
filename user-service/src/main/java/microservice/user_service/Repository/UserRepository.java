package microservice.user_service.Repository;

import microservice.user_service.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :userId")
    Optional<User> findByIdWithRoles(@Param("userId") Long userId);

    Optional<User> findByUsername(String accountNumber);
    Optional<User> findByPhoneNumber(String accountNumber);
    Optional<User> findByEmail(String accountNumber);

}
