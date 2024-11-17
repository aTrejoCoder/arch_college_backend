package microservice.common_classes.FacadeService.Grade;

import microservice.common_classes.DTOs.Grade.GradeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("GradeFacadeServiceImpl")
public class GradeFacadeServiceImpl implements  GradeFacadeService {

    @Override
    public CompletableFuture<GradeDTO> getGradeById(Long gradeId) {
        return null;
    }

    @Override
    public CompletableFuture<List<GradeDTO>> getGradesByStudentId(Long studentId) {
        return null;
    }
}
