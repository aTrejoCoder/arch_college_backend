package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Result;
import microservice.enrollment_service.DTOs.EnrollmentDTO;
import microservice.enrollment_service.DTOs.EnrollmentInsertDTO;

import java.util.List;

public interface EnrollmentService {
    Result<EnrollmentDTO> getEnrollmentById(Long enrollmentId);
    void createEnrollment(EnrollmentInsertDTO enrollmentInsertDTO);
    void deleteEnrollment(Long enrollmentId);
    List<EnrollmentDTO> getEnrollmentsByAccountNumber(String studentAccountNumber);
}
