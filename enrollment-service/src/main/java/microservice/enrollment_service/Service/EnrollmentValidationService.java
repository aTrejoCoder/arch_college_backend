package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;

public interface EnrollmentValidationService {
    Result<Void> validateEnrollment(EnrollmentInsertDTO enrollmentInsertDTO, EnrollmentRelationshipDTO enrollmentRelationshipDTO, String accountNumber);
}
