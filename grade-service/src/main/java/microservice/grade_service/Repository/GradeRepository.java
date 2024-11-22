package microservice.grade_service.Repository;

import microservice.grade_service.Model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentAccountNumber(String accountNumber);
    List<Grade> findByStudentAccountNumberAndOrdinarySubjectId(String accountNumber, Long ordinarySubjectId);
    List<Grade> findByStudentAccountNumberAndElectiveSubjectId(String accountNumber, Long electiveSubjectId);
}
