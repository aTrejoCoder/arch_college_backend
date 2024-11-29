package microservice.common_classes.FacadeService.Student;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.Utils.CustomPage;
import microservice.common_classes.Utils.ProfessionalLineModality;

import java.util.concurrent.CompletableFuture;

public interface StudentFacadeService {
    CompletableFuture<Boolean> validateExisitingStudentAsync(String accountNumber);
    CompletableFuture<StudentDTO> getStudentByAccountNumberAsync(String accountNumber);
    CompletableFuture<Void> increaseSemesterCompletedAsync(String studentAccount);
    CompletableFuture<Void> setProfessionalLineDataAsync(String studentAccount, Long professionalLineId, ProfessionalLineModality professionalLineModality);

    CustomPage<StudentDTO> getStudentsPageable(int page, int pageSize);
}
