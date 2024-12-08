package microservice.academic_curriculum_service.Repository;

import microservice.academic_curriculum_service.Model.Career.Area;
import microservice.academic_curriculum_service.Model.Subject.SubjectSeries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubjectSeriesRepository extends JpaRepository<SubjectSeries, Long> {
        Page<SubjectSeries> findAll(Pageable pageable);
}
