package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationship;

public interface EnrollmentCommandService {
    void createEnrollment(EnrollmentRelationship enrollmentRelationship, EnrollmentInsertDTO enrollmentInsertDTO);
    Result<Void> deleteEnrollment(String groupKey, String subjectKey, String studentAccountNumber);
    void deleteEnrollment(Long enrollmentId);
}
