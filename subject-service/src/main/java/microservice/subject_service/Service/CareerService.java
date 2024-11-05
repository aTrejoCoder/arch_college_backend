package microservice.subject_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Career.CareerDTO;
import microservice.subject_service.DTOs.Career.CareerInsertDTO;

import java.util.List;

public interface CareerService {
    Result<CareerDTO> getCareerById(Long careerId);
    Result<CareerDTO> getCareerByIdWithSubjects(Long careerId);
    Result<CareerDTO> getCareerByName(String name);
    List<CareerDTO> getAllCareers();
    void createCareer(CareerInsertDTO careerInsertDTO);
    void updateCareer(CareerInsertDTO careerInsertDTO, Long careerId);
    void deleteCareer(Long careerId);
}
