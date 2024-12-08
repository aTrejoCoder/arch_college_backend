package microservice.academic_curriculum_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Carrer.CareerInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.academic_curriculum_service.Mappers.CareerMapper;
import microservice.academic_curriculum_service.Model.Career.Career;
import microservice.academic_curriculum_service.Repository.CareerRepository;
import microservice.academic_curriculum_service.Service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class CareerServiceImpl implements CareerService {
    private final CareerRepository careerRepository;
    private final CareerMapper careerMapper;
    private final KeyGenerationService keyGenerationService;

    @Autowired
    public CareerServiceImpl(CareerRepository careerRepository,
                             CareerMapper careerMapper,
                             KeyGenerationService keyGenerationService) {
        this.careerRepository = careerRepository;
        this.careerMapper = careerMapper;
        this.keyGenerationService = keyGenerationService;
    }

    @Override
    @Cacheable(value = "careerByIdCache", key = "#careerId")
    public Result<CareerDTO> getCareerById(Long careerId) {
        Optional<Career> optionalCareer = careerRepository.findById(careerId);
        return optionalCareer.map(career -> Result.success(careerMapper.entityToDTO(career)))
                .orElseGet(() -> Result.error("Career with ID " + careerId + " not found"));
    }

    @Override
    @Cacheable(value = "careerWithSubjectsCache", key = "#careerId")
    public Result<CareerDTO> getCareerByIdWithSubjects(Long careerId) {
        Optional<Career> optionalCareer = careerRepository.findById(careerId);
        if (optionalCareer.isEmpty()) {
            return Result.error("Career with ID " + careerId + " not found");
        }
        Career career = optionalCareer.get();

        return Result.success(careerMapper.entityToDTO(career));
    }

    @Override
    @Cacheable(value = "careerByNameCache", key = "#name")
    public Result<CareerDTO> getCareerByName(String name) {
        Optional<Career> optionalCareer = careerRepository.findByName(name);
        return optionalCareer.map(career -> Result.success(careerMapper.entityToDTO(career)))
                .orElseGet(() -> Result.error("Career with name " + name + " not found"));
    }

    @Override
    @Cacheable(value = "allCareersCache")
    public List<CareerDTO> getAllCareers() {
        return careerRepository.findAll().stream().map(careerMapper::entityToDTO).toList();
    }
    @Override
    public void createCareer(CareerInsertDTO careerInsertDTO) {
        Career career = careerMapper.insertDtoToEntity(careerInsertDTO);
        career.setKey(keyGenerationService.generateCareerKey(career));

        careerRepository.save(career);
    }

    @Override
    public void updateCareer(CareerInsertDTO careerInsertDTO, Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Career with ID " + careerId + " not found"));

        careerMapper.updateEntity(career, careerInsertDTO);
        careerRepository.save(career);
    }
}
