package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentAccountNumberAndEnrollmentPeriod(String accountNumber, String enrollmentPeriod);

    Optional<Enrollment> findByGroupKeyAndSubjectKeyAndStudentAccountNumber(String groupKey, String subjectKey, String accountNumber);
}
