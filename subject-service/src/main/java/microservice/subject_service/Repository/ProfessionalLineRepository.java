package microservice.subject_service.Repository;

import microservice.subject_service.Model.ProfessionalLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalLineRepository extends JpaRepository<ProfessionalLine, Long> {
    Optional<ProfessionalLine> findByName(String name);
}
