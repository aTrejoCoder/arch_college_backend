package microservice.academic_curriculum_service.Repository;

import microservice.academic_curriculum_service.Model.Career.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByName(String name);
    @Query("SELECT a FROM Area a " +
            "LEFT JOIN FETCH a.obligatorySubjects os " +
            "LEFT JOIN FETCH a.electiveSubjects es " +
            "WHERE a.id = :areaId")
    Page<Area> findByIdWithSubjects(Long areaId, Pageable pageable);

}
