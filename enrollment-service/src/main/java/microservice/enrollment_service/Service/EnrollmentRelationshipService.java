package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;

public interface EnrollmentRelationshipService {
    Result<EnrollmentRelationshipDTO> validateAndGetRelationships(EnrollmentInsertDTO enrollmentInsertDTO, String studentAccountNumber);
    Result<Void> takeSpot(String groupKey);
}
