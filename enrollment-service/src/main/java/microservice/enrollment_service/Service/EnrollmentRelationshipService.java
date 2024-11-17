package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Result;
import microservice.enrollment_service.DTOs.EnrollmentDTO;
import microservice.enrollment_service.DTOs.EnrollmentInsertDTO;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;

import java.util.List;

public interface EnrollmentRelationshipService {
    Result<EnrollmentRelationshipDTO> validateAndGetRelationships(EnrollmentInsertDTO enrollmentInsertDTO);
    Result<Void> takeSpot(Long groupId);
}
