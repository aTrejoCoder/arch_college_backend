package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.Utils.Response.Result;

import java.util.List;

public interface EnrollmentFinderService {
    Result<EnrollmentDTO> getById(Long id);
    List<EnrollmentDTO> getByAccountNumber(String studentAccountNumber);
}
