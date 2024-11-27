package microservice.grade_service.Service;

import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.Result;
import microservice.grade_service.DTOs.GradeInsertDTO;

import java.util.List;

public interface GradeService {
    Result<GradeDTO> getGradeById(Long enrollmentId);
    void initGrade(GradeInsertDTO gradeInsertDTO);
    void deleteGradeById(Long enrollmentId);
    List<GradeDTO> getAllGradeByStudentAccountNumber(String accountNumber);
    List<GradeDTO> getGradesByStudentAccountNumberAndSchoolPeriod(String accountNumber, String schoolPeriod);
}
