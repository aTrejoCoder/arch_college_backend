package microservice.grade_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.grade_service.DTOs.GradeWithRelationsDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;

import java.util.List;

public interface GradeService {
    Result<GradeWithRelationsDTO> getGradeById(Long enrollmentId);
    void initGrade(GradeInsertDTO gradeInsertDTO);
    void deleteGradeById(Long enrollmentId);
    List<GradeWithRelationsDTO> getAllGradeByStudentAccountNumber(String accountNumber);
    List<GradeWithRelationsDTO> getGradesByStudentAccountNumberAndSchoolPeriod(String accountNumber, String schoolPeriod);
}
