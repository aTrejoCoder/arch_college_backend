package microservice.common_classes.FacadeService.Grade;

import microservice.common_classes.DTOs.Grade.GradeDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GradeFacadeService {
    CompletableFuture<GradeDTO> getGradeById(Long gradeId);
    CompletableFuture<List<GradeDTO>> getGradesByStudentId(Long studentId);

}