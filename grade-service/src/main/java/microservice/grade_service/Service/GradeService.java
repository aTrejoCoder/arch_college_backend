package microservice.grade_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.grade_service.DTOs.GradeDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;

import java.util.List;

public interface GradeService {
    Result<GradeDTO> getGradeById(Long enrollmentId);
    Result<Void> authorizeGradeById(Long gradeId);
    void initGrade(GradeInsertDTO gradeInsertDTO);
    void deleteGradeById(Long enrollmentId);
    Result<Void> validateGradeRelationships(GradeInsertDTO gradeInsertDTO);
    List<GradeDTO> getAllGradeByStudent(Long studentId);
    List<GradeDTO> getGradesByStudentAndSchoolPeriod(Long studentId, String schoolPeriod);
    Result<Void> validateGradeCreation(GradeInsertDTO gradeInsertDTO);
}
