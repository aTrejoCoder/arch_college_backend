package microservice.grade_service.Service;

import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.grade_service.Model.AcademicHistory;
import microservice.grade_service.Model.Grade;

public interface AcademicHistoryService {
    void validateUniqueAcademicHistoryPerStudent(String accountNumber);
    void validateGrade(Grade grade);

    void initAcademicHistory(StudentDTO studentDTO, CareerDTO careerDTO);
    AcademicHistory getAcademicHistoryByAccountNumber(String accountNumber);
    void setGradeToAcademicHistory(Grade grade);
}
