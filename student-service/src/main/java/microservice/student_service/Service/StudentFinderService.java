package microservice.student_service.Service;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Student.StudentFilter;
import microservice.common_classes.Utils.Student.StudentFinderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentFinderService {
    Result<StudentDTO> getStudentById(Long studentId);
    Result<StudentDTO> getStudentByAccountNumber(String accountNumber);

    Page<StudentDTO> getAllStudentsSortedByFilterPageable(Pageable pageable, StudentFilter filter);
    Page<StudentDTO> getStudentsSortedByFilterPageable(Pageable pageable, StudentFilter filter, String param);
}
