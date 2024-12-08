package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationship;

public interface EnrollmentValidationService {
    Result<Void> validateEnrollment(EnrollmentInsertDTO enrollmentInsertDTO, EnrollmentRelationship enrollmentRelationship, String accountNumber);
}
