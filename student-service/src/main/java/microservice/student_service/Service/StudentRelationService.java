package microservice.student_service.Service;

import microservice.common_classes.Utils.Result;

public interface StudentRelationService {
    Result<Void> validateExistingCareerId(Long careerId);
    Result<Void> validateProfessionalLineId(Long professionalLine);
}
