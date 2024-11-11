package microservice.user_service.Repository;

import microservice.user_service.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String accountNumber);
    Optional<User> findByPhoneNumber(String accountNumber);
    Optional<User> findByEmail(String accountNumber);

}
