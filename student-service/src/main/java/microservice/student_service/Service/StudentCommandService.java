package microservice.student_service.Service;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Student.StudentInsertDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;

public interface StudentCommandService {
    void increaseSemestersCursed(String accountNumber);
    void setProfessionalLineData(String accountNumber, Long professionalLineId, ProfessionalLineModality professionalLineModality);

    StudentDTO createStudent(StudentInsertDTO studentInsertDTO);
    void updateStudent(StudentInsertDTO studentInsertDTO,  Long studentId);
    void deleteStudent(Long studentId);

    boolean validateExistingStudent(String accountNumber);
}
