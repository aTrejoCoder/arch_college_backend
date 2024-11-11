package microservice.common_classes.FacadeService;

import microservice.common_classes.DTOs.Student.StudentDTO;

public interface StudentFacadeService {
    boolean validateExisitingStudent(Long studentId);
    StudentDTO getStudentById(Long studentId);
}
