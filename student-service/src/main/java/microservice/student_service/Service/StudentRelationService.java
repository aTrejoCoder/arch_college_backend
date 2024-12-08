package microservice.student_service.Service;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.Utils.Response.Result;

public interface StudentRelationService {
    Result<Void> validateExistingCareerId(Long careerId);
    Result<Void> validateProfessionalLineId(Long professionalLine);

    void initAcademicHistoryAsync(StudentDTO studentDTO);
}
