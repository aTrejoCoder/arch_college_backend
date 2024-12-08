package microservice.common_classes.FacadeService.Grade;

import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.CustomPage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GradeFacadeService {
    CompletableFuture<GradeDTO> getGradeById(Long gradeId);
    CompletableFuture<List<GradeDTO>> getGradesByStudentAccountNumber(String accountNumber);
    CustomPage<GradeDTO> getGradesByCareerPageable(int page, int pageSize);

}