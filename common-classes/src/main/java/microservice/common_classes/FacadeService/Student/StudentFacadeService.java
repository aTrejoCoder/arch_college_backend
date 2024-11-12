package microservice.common_classes.FacadeService.Student;

import microservice.common_classes.DTOs.Student.StudentDTO;

import java.util.concurrent.CompletableFuture;

public interface StudentFacadeService {
    CompletableFuture<Boolean> validateExisitingStudent(Long studentId);
    CompletableFuture<Boolean> validateExisitingStudent(String accountNumber);
    CompletableFuture<StudentDTO> getStudentById(Long studentId);
    CompletableFuture<StudentDTO> getStudentByAccountNumber(String accountNumber);


}
