package microservice.common_classes.FacadeService.Student;

import microservice.common_classes.DTOs.Student.StudentDTO;

import java.util.concurrent.CompletableFuture;

public interface StudentFacadeService {
    CompletableFuture<Boolean> validateExisitingStudent(Long studentId);
    CompletableFuture<StudentDTO> getStudentById(Long studentId);
}
