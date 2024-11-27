package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;

import java.util.List;

public interface EnrollmentService {
    Result<EnrollmentDTO> getEnrollmentById(Long enrollmentId);
    void createEnrollment(EnrollmentRelationshipDTO enrollmentRelationshipDTO, EnrollmentInsertDTO enrollmentInsertDTO);
    Result<Void> deleteEnrollment(String groupKey, String subjectKey, String studentAccountNumber);
    void deleteEnrollment(Long enrollmentId);

    List<EnrollmentDTO> getEnrollmentsByAccountNumber(String studentAccountNumber);
}
