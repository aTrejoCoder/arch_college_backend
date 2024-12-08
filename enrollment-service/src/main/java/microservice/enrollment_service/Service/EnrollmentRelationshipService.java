package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationship;
import microservice.enrollment_service.Model.Preload.Group;

public interface EnrollmentRelationshipService {
    Result<Group> validateExistingGroup(EnrollmentInsertDTO enrollmentInsertDTO);
    EnrollmentRelationship getRelationshipData(Group group, String accountNumber);
    Result<Void> takeSpot(Long groupId);
}
