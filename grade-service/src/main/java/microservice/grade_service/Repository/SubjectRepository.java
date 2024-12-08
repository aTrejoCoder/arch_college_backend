package microservice.grade_service.Repository;

import microservice.common_classes.Utils.SubjectType;
import microservice.grade_service.Model.Group;
import microservice.grade_service.Model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findBySubjectIdAndSubjectType(Long subjectId, SubjectType subjectType);
}
