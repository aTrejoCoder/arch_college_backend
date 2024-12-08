package microservice.academic_curriculum_service.Repository;

import microservice.academic_curriculum_service.Model.Career.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Long> {
    Optional<Career> findByName(String name);
}
