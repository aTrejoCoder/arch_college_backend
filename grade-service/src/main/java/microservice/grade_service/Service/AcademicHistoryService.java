package microservice.grade_service.Service;

import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Student.StudentExtendedDTO;
import microservice.grade_service.Model.AcademicHistory;

public interface AcademicHistoryService {
    void initAcademicHistory(StudentExtendedDTO studentExtendedDTO, CareerDTO careerDTO);
    AcademicHistory  getAcademicHistoryByAccountNumber(String accountNumber);
}
