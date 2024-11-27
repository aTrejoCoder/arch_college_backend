package microservice.student_service.Service;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Student.StudentInsertDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;
import microservice.common_classes.Utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    Result<StudentDTO> getStudentById(Long studentId);
    Page<StudentDTO> getAllStudentsSortedByLastname(Pageable pageable, String sortDirection);
    Page<StudentDTO> getAllStudentsSortedByAccountNumber(Pageable pageable, String sortDirection);
    Result<StudentDTO> getStudentByAccountNumber(String accountNumber);

    void increaseSemestersCursed(String accountNumber);
    void setProfessionalLineData(String accountNumber, Long professionalLineId, ProfessionalLineModality professionalLineModality);

    StudentDTO createStudent(StudentInsertDTO studentInsertDTO);
    void updateStudent(StudentInsertDTO studentInsertDTO,  Long studentId);
    void deleteStudent(Long studentId);

    boolean validateExistingStudent(Long studentId);
}
