package microservice.grade_service.Repository;

import microservice.common_classes.Utils.Grades.GradeStatus;
import microservice.common_classes.Utils.SubjectType;
import microservice.grade_service.Model.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface GradeRepository extends JpaRepository<Grade, Long>, JpaSpecificationExecutor<Grade> {
    List<Grade> findByStudentAccountNumberAndSchoolPeriod(String accountNumber, String schoolPeriod);
    Page<Grade> findByGradeStatus(GradeStatus status, Pageable pageable);
    List<Grade> findByStudentAccountNumberAndSubjectIdAndSubject_SubjectType(String accountNumber, Long subjectId, SubjectType subjectType);
}
