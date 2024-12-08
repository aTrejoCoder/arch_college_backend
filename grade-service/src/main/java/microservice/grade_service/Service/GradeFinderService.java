package microservice.grade_service.Service;

import microservice.grade_service.DTOs.GradeDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.Utils.Credits.GradeFinderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GradeFinderService {
    Result<GradeDTO> getGradeById(Long enrollmentId);
    Page<GradeDTO> getPendingValidationGrades(Pageable pageable);
    Page<GradeDTO> getGradesByFilters(GradeFinderFilter gradeFilter, Pageable pageable);
    List<GradeDTO> getAnnuallyGradesByStudentAccountNumber(String studentAccountNumber);
    List<GradeDTO> getCurrentGradesByStudentAccountNumber(String studentAccountNumber);
}
