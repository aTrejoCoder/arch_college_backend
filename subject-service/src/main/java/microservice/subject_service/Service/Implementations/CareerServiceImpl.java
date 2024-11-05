package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Career.CareerDTO;
import microservice.subject_service.DTOs.Career.CareerInsertDTO;
import microservice.subject_service.Mappers.CareerMapper;
import microservice.subject_service.Model.Career;
import microservice.subject_service.Repository.CareerRepository;
import microservice.subject_service.Service.CareerService;
import org.jvnet.hk2.annotations.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CareerServiceImpl implements CareerService {
    private final CareerRepository careerRepository;
    private final CareerMapper careerMapper;
    private final CareerDomainService careerDomainService;

    public CareerServiceImpl(CareerRepository careerRepository,
                             CareerMapper careerMapper,
                             CareerDomainService careerDomainService) {
        this.careerRepository = careerRepository;
        this.careerMapper = careerMapper;
        this.careerDomainService = careerDomainService;
    }

    @Override
    public Result<CareerDTO> getCareerById(Long careerId) {
        Optional<Career> optionalCareer = careerRepository.findById(careerId);
        return optionalCareer.map(career ->  Result.success(careerMapper.entityToDTO(career)))
                .orElseGet(() -> Result.error("Career with ID" + careerId + " not found"));
    }

    @Override
    public Result<CareerDTO> getCareerByIdWithSubjects(Long careerId) {
        Optional<Career> optionalCareer = careerRepository.findById(careerId);
        if (optionalCareer.isEmpty()) {
            return  Result.error("Career with ID" + careerId + " not found");
        }
        Career career = optionalCareer.get();

        return Result.success(careerMapper.entityToDTO(career));
    }

    @Override
    public Result<CareerDTO> getCareerByName(String name) {
        Optional<Career> optionalCareer = careerRepository.findByName(name);
        return optionalCareer.map(career ->  Result.success(careerMapper.entityToDTO(career)))
                .orElseGet(() -> Result.error("Career with name" + name + " not found"));
    }

    @Override
    public List<CareerDTO> getAllCareers() {
        return careerRepository.findAll().stream().map(careerMapper::entityToDTO).toList();
    }

    @Override
    public void createCareer(CareerInsertDTO careerInsertDTO) {
        Career career = careerMapper.insertDtoToEntity(careerInsertDTO);
        career.setKey(careerDomainService.generateKey(career));

        careerRepository.save(career);
    }

    @Override
    public void updateCareer(CareerInsertDTO careerInsertDTO, Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Career with ID" + careerId + " not found"));

        careerMapper.updateEntity(career, careerInsertDTO);
        careerRepository.save(career);
    }

    @Override
    public void deleteCareer(Long careerId) {
        if (!careerRepository.existsById(careerId)) {
            throw new EntityNotFoundException("Career with ID" + careerId + " not found");
        }

        careerRepository.deleteById(careerId);
    }
}
