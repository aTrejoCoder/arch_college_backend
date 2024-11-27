package microservice.grade_service.Service;

import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.grade_service.Model.AcademicHistory;

import java.util.List;

public interface AcademicHistoryService {
    void validateUniqueAcademicHistoryPerStudent(String accountNumber);
    void initAcademicHistory(StudentDTO studentDTO, CareerDTO careerDTO);
    AcademicHistory getAcademicHistoryByAccountNumber(String accountNumber);

}
