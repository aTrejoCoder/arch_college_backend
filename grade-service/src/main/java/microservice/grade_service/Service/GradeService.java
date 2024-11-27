package microservice.grade_service.Service;

import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.DTOs.GradeInsertDTO;

import java.util.List;

public interface GradeService {
    Result<GradeDTO> getGradeById(Long enrollmentId);
    void initGrade(GradeInsertDTO gradeInsertDTO);
    void deleteGradeById(Long enrollmentId);

    List<GradeDTO> getAllGradeByStudentAccountNumber(String accountNumber);
    List<GradeDTO> getGradesByStudentAccountNumber(String accountNumber , String schoolPeriod);
    List<GradeDTO> getAnnuallyGradesByStudentAccountNumber(String accountNumber);
    List<GradeDTO> getCurrentGradesByStudentAccountNumber(String accountNumber);
}
