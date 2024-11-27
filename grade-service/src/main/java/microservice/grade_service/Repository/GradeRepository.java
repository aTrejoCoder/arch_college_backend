package microservice.grade_service.Repository;

import microservice.common_classes.Utils.SubjectType;
import microservice.grade_service.Model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentAccountNumber(String accountNumber);
    List<Grade> findByStudentAccountNumberAndSchoolPeriod(String accountNumber, String schoolPeriod);
    List<Grade> findByStudentAccountNumberAndSubjectIdAndSubjectType(String accountNumber, Long subjectId, SubjectType subjectType);


}
