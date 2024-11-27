package microservice.grade_service.Service;

import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.DTOs.GradeInsertDTO;

public interface GradeValidationService {
    Result<Void> authorizeGradeById(Long gradeId);
    Result<Void> validateGradeCreation(GradeInsertDTO gradeInsertDTO);
}
