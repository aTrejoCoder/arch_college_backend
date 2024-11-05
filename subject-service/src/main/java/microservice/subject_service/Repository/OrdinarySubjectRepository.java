package microservice.subject_service.Repository;

import microservice.subject_service.Model.OrdinarySubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdinarySubjectRepository extends JpaRepository<OrdinarySubject, Long> {
    Optional<OrdinarySubject> findByName(String name);
    Page<OrdinarySubject> findBySemester(int semester, Pageable pageable);
    Page<OrdinarySubject> findByAreaId(Long areaId, Pageable pageable);

    Page<OrdinarySubject> findByAll(Pageable pageable);
}
