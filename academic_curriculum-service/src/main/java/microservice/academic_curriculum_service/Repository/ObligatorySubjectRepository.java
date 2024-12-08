package microservice.academic_curriculum_service.Repository;

import microservice.academic_curriculum_service.Model.Subject.ObligatorySubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ObligatorySubjectRepository extends JpaRepository<ObligatorySubject, Long> {
    Optional<ObligatorySubject> findByName(String name);
    Page<ObligatorySubject> findBySemester(int semester, Pageable pageable);
    Page<ObligatorySubject> findByAreaId(Long areaId, Pageable pageable);
    Page<ObligatorySubject> findByCareerId(Long careerId, Pageable pageable);


    List<ObligatorySubject> findByCareerId(Long careerId);
    List<ObligatorySubject> findByIdIn(Set<Long> ids);
    List<ObligatorySubject> findBySemester(int semester);
    Page<ObligatorySubject> findAll(Pageable pageable);
}
