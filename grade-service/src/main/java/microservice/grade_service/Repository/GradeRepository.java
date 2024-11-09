package microservice.grade_service.Repository;

import microservice.grade_service.Model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByStudentIdAndSchoolPeriod(Long studentId, String schoolPeriod);
    List<Grade> findByStudentIdAndOrdinarySubjectId(Long studentId, Long ordinarySubjectId);
    List<Grade> findByStudentIdAndElectiveSubjectId(Long studentId, Long electiveSubjectId);
}
