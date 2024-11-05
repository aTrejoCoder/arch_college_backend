package microservice.subject_service.Repository;

import microservice.subject_service.Model.ElectiveSubject;
import microservice.subject_service.Model.OrdinarySubject;
import microservice.subject_service.Model.ProfessionalLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectiveSubjectRepository extends JpaRepository<ElectiveSubject, Long> {
    Optional<ElectiveSubject> findByName(String name);
    Page<ElectiveSubject> findByProfessionalLine(Long professionalLineId, Pageable pageable);
    Page<ElectiveSubject> findByAreaId(Long areaId, Pageable pageable);

    Page<ElectiveSubject> findByAll(Pageable pageable);
}
