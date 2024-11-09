package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.GroupEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface EnrollmentRepository extends JpaRepository<GroupEnrollment, Long> {
    List<GroupEnrollment> findByStudentIdAndEnrollmentPeriod(Long studentId, String enrollmentPeriod);


    @Modifying
    @Query("UPDATE GroupEnrollment e SET e.isActive = false WHERE e.enrollmentDate < :expirationDate AND e.isActive = true")
    void deactivateExpiredEnrollments(LocalDateTime expirationDate);
}
