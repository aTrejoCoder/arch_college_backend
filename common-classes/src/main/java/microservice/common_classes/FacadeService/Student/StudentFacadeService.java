package microservice.common_classes.FacadeService.Student;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;

import java.util.concurrent.CompletableFuture;

public interface StudentFacadeService {
    CompletableFuture<Boolean> validateExisitingStudent(String accountNumber);
    CompletableFuture<StudentDTO> getStudentByAccountNumber(String accountNumber);
    CompletableFuture<Void> increaseSemesterCompleted(String studentAccount);
    CompletableFuture<Void> setProfessionalLineData(String studentAccount, Long professionalLineId, ProfessionalLineModality professionalLineModality);
}
