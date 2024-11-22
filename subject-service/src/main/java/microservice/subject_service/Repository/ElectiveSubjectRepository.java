package microservice.subject_service.Repository;

import microservice.subject_service.Model.ElectiveSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ElectiveSubjectRepository extends JpaRepository<ElectiveSubject, Long> {
    Optional<ElectiveSubject> findByName(String name);
    Page<ElectiveSubject> findByProfessionalLine(Long professionalLineId, Pageable pageable);
    Page<ElectiveSubject> findByAreaId(Long areaId, Pageable pageable);
    Page<ElectiveSubject> findByCareerId(Long careerId, Pageable pageable);

    List<ElectiveSubject> findByCareerId(Long careerId);

    @Query("SELECT COUNT(es) FROM ElectiveSubject es WHERE es.professionalLine.id = ?1")
    int countByProfessionalLineId(Long professionalLineId);

    Page<ElectiveSubject> findAll(Pageable pageable);
}
