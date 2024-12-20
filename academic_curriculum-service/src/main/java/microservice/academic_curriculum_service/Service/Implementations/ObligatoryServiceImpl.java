package microservice.academic_curriculum_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.academic_curriculum_service.Model.Subject.ObligatorySubject;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.academic_curriculum_service.Mappers.ObligatorySubjectMapper;
import microservice.academic_curriculum_service.Model.Career.Area;
import microservice.academic_curriculum_service.Model.Career.Career;
import microservice.academic_curriculum_service.Repository.AreaRepository;
import microservice.academic_curriculum_service.Repository.CareerRepository;
import microservice.academic_curriculum_service.Repository.ObligatorySubjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import microservice.academic_curriculum_service.Service.SubjectService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ObligatoryServiceImpl implements SubjectService<ObligatorySubjectDTO, ObligatorySubjectInsertDTO> {

    private final ObligatorySubjectRepository obligatorySubjectRepository;
    private final ObligatorySubjectMapper obligatorySubjectMapper;
    private final CareerRepository careerRepository;
    private final AreaRepository areaRepository;
    private final KeyGenerationService keyGenerationService;

    @Autowired
    public ObligatoryServiceImpl(ObligatorySubjectRepository obligatorySubjectRepository,
                                 ObligatorySubjectMapper obligatorySubjectMapper,
                                 CareerRepository careerRepository,
                                 AreaRepository areaRepository,
                                 KeyGenerationService keyGenerationService) {
        this.obligatorySubjectRepository = obligatorySubjectRepository;
        this.obligatorySubjectMapper = obligatorySubjectMapper;
        this.careerRepository = careerRepository;
        this.areaRepository = areaRepository;
        this.keyGenerationService = keyGenerationService;
    }

    @Override
    @Cacheable(value = "obligatorySubjectByIdCache", key = "#subjectId")
    public Result<ObligatorySubjectDTO> getSubjectById(Long subjectId) {
        Optional<ObligatorySubject> optionalSubject = obligatorySubjectRepository.findById(subjectId);
        return optionalSubject.map(subject -> Result.success(obligatorySubjectMapper.entityToDTO(subject)))
                .orElseGet(() -> Result.error("AcademicCurriculumService with ID " + subjectId + " not found"));
    }


    @Override
    @Cacheable(value = "obligatorySubjectByNameCache", key = "#name")
    public Result<ObligatorySubjectDTO> getSubjectByName(String name) {
        Optional<ObligatorySubject> optionalSubject = obligatorySubjectRepository.findByName(name);
        return optionalSubject.map(subject -> Result.success(obligatorySubjectMapper.entityToDTO(subject)))
                .orElseGet(() -> Result.error("AcademicCurriculumService with name " + name + " not found"));
    }

    @Override
    public Result<List<ObligatorySubjectDTO>> getSubjectByIdsIn(Set<Long> providedIds) {
        List<Long> missingIds;

        List<ObligatorySubject> obligatorySubjects = obligatorySubjectRepository.findByIdIn(providedIds);

        List<Long> foundIds = obligatorySubjects.stream()
                .map(ObligatorySubject::getId)
                .toList();

        missingIds = providedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            return Result.error("Some IDs were not found:" + missingIds.toString());
        }

        List<ObligatorySubjectDTO> obligatorySubjectDTOS = obligatorySubjects.stream()
                .map(obligatorySubjectMapper::entityToDTO)
                .toList();

        return Result.success(obligatorySubjectDTOS);
    }

    @Override
    @Cacheable(value = "obligatorySubjectsByFilterCache", key = "#filterId + '_' + #filterType")
    public Page<ObligatorySubjectDTO> getSubjectsByFilterPageable(Long filterId, String filterType, Pageable pageable) {
        return switch (filterType) {
            case "semester" -> {
                Page<ObligatorySubject> semesterSubjects = obligatorySubjectRepository.findBySemester(
                        filterId.intValue(), pageable);
                yield semesterSubjects.map(obligatorySubjectMapper::entityToDTO);
            }
            case "area" -> {
                Page<ObligatorySubject> areaSubjects = obligatorySubjectRepository.findByAreaId(filterId, pageable);
                yield areaSubjects.map(obligatorySubjectMapper::entityToDTO);
            }
            case "career" -> {
                Page<ObligatorySubject> careerSubjects = obligatorySubjectRepository.findByCareerId(filterId, pageable);
                yield careerSubjects.map(obligatorySubjectMapper::entityToDTO);
            }
            default -> throw new IllegalArgumentException("Invalid filter type: " + filterType);
        };
    }

    @Override
    @Cacheable(value = "allObligatorySubjectsCache")
    public Page<ObligatorySubjectDTO> getAllSubjectsPageable(Pageable pageable) {
        return obligatorySubjectRepository.findAll(pageable).map(obligatorySubjectMapper::entityToDTO);
    }

    @Override
    public List<ObligatorySubjectDTO> getSubjectsByFilter(Long filterId, String filterType) {
        return switch (filterType) {
            case "career" -> obligatorySubjectRepository.findByCareerId(filterId).stream()
                    .map(obligatorySubjectMapper::entityToDTO)
                    .toList();
            default -> throw new IllegalArgumentException("Invalid filter type: " + filterType);
        };
    }

    @Override
    @Transactional
    public void createSubject(ObligatorySubjectInsertDTO obligatorySubjectInsertDTO) {
        ObligatorySubject obligatorySubject = obligatorySubjectMapper.insertDtoToEntity(obligatorySubjectInsertDTO);

        handleObligatorySubjectRelationships(obligatorySubject, obligatorySubjectInsertDTO);

        obligatorySubjectRepository.saveAndFlush(obligatorySubject);

        String key = keyGenerationService.generateSubjectKey(obligatorySubject);
        obligatorySubject.setKey(key);

        obligatorySubjectRepository.save(obligatorySubject);
    }

    @Override
    @Transactional
    public void updateSubject(ObligatorySubjectInsertDTO obligatorySubjectInsertDTO, Long subjectId) {
        ObligatorySubject obligatorySubject = obligatorySubjectMapper.updateDtoToEntity(obligatorySubjectInsertDTO, subjectId);
        obligatorySubjectRepository.save(obligatorySubject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long subjectId) {
        if (!obligatorySubjectRepository.existsById(subjectId)) {
            throw new EntityNotFoundException("AcademicCurriculumService with ID" + subjectId + " not found");
        }
        obligatorySubjectRepository.deleteById(subjectId);
    }

    private void handleObligatorySubjectRelationships(ObligatorySubject subject, ObligatorySubjectInsertDTO obligatorySubjectInsertDTO) {
        getAndSetCareer(subject, obligatorySubjectInsertDTO.getCareerId());
        getAndSetArea(subject, obligatorySubjectInsertDTO.getAreaId());
    }

    private void getAndSetCareer(ObligatorySubject subject, Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Career with ID " + careerId + " not found"));
        subject.setCareer(career);
    }

    private void getAndSetArea(ObligatorySubject subject, Long areaId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException("Area with ID " + areaId + " not found"));
        subject.setArea(area);
    }

}
