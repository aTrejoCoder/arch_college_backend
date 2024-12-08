package microservice.grade_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Model.Group;

import java.util.List;

public interface GradeCommandService {
    void initGradesFromEnrollments(Group group, List<EnrollmentDTO> enrollmentDTOS);
    void deleteGradeById(Long enrollmentId);
}
