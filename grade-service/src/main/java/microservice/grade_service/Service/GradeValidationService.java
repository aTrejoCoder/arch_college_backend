package microservice.grade_service.Service;

import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.DTOs.TeacherQualificationDTO;

public interface GradeValidationService {
    Result<Void> authorizeGradeById(Long gradeId);
    Result<Void> validateGrade(Long gradeId);
}
