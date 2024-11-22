package microservice.subject_service.Repository;

import microservice.subject_service.Model.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Long> {
    Optional<Career> findByName(String name);
}
