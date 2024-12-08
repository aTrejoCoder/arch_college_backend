package microservice.academic_curriculum_service.Repository;

import microservice.academic_curriculum_service.Model.Career.ProfessionalLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalLineRepository extends JpaRepository<ProfessionalLine, Long> {
    Optional<ProfessionalLine> findByName(String name);
}
