package microservice.common_classes.FacadeService.Grade;

import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.DTOs.Grade.InitAcademicHistory;
import microservice.common_classes.DTOs.Student.StudentDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GradeFacadeService {
    CompletableFuture<GradeDTO> getGradeById(Long gradeId);
    CompletableFuture<List<GradeDTO>> getGradesByStudentAccountNumber(String accountNumber);
}