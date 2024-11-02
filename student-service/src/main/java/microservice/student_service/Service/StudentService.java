package microservice.student_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.student_service.DTOs.StudentDTO;
import microservice.student_service.DTOs.StudentInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    Result<StudentDTO> getStudentById(Long studentId);
    Page<StudentDTO> getAllStudentsSortedByLastname(Pageable pageable, String sortDirection);
    Page<StudentDTO> getAllStudentsSortedByAccountNumber(Pageable pageable, String sortDirection);
    Result<StudentDTO> getStudentByAccountNumber(String accountNumber);

    void createStudent(StudentInsertDTO studentInsertDTO);
    void updateStudent(StudentInsertDTO studentInsertDTO,  Long studentId);
    void deleteStudent(Long studentId);

    boolean validateExistingStudent(Long studentId);
}
