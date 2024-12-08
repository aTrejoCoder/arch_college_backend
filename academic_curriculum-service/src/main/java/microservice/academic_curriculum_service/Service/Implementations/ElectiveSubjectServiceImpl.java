package microservice.academic_curriculum_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import microservice.academic_curriculum_service.Model.Career.Area;
import microservice.academic_curriculum_service.Model.Career.Career;
import microservice.academic_curriculum_service.Model.Career.ProfessionalLine;
import microservice.academic_curriculum_service.Model.Subject.ElectiveSubject;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.academic_curriculum_service.Mappers.ElectiveSubjectMapper;
import microservice.academic_curriculum_service.Repository.AreaRepository;
import microservice.academic_curriculum_service.Repository.CareerRepository;
import microservice.academic_curriculum_service.Repository.ElectiveSubjectRepository;
import microservice.academic_curriculum_service.Repository.ProfessionalLineRepository;
import microservice.academic_curriculum_service.Service.SubjectService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ElectiveSubjectServiceImpl implements SubjectService<ElectiveSubjectDTO, ElectiveSubjectInsertDTO> {

    private final ElectiveSubjectRepository electiveSubjectRepository;
    private final ElectiveSubjectMapper electiveSubjectMapper;
    private final AreaRepository areaRepository;
    private final CareerRepository careerRepository;
    private final ProfessionalLineRepository professionalLineRepository;
    private final KeyGenerationService keyGenerationService;

    @Autowired
    public ElectiveSubjectServiceImpl(ElectiveSubjectRepository electiveSubjectRepository,
                                      ElectiveSubjectMapper electiveSubjectMapper,
                                      AreaRepository areaRepository,
                                      CareerRepository careerRepository,
                                      ProfessionalLineRepository professionalLineRepository,
                                      KeyGenerationService keyGenerationService) {
        this.electiveSubjectRepository = electiveSubjectRepository;
        this.electiveSubjectMapper = electiveSubjectMapper;
        this.areaRepository = areaRepository;
        this.careerRepository = careerRepository;
        this.professionalLineRepository = professionalLineRepository;
        this.keyGenerationService = keyGenerationService;
    }

    @Override
    @Cacheable(value = "electiveSubjectByIdCache", key = "#subjectId")
    public Result<ElectiveSubjectDTO> getSubjectById(Long subjectId) {
        Optional<ElectiveSubject> optionalSubject = electiveSubjectRepository.findById(subjectId);
        return optionalSubject.map(electiveSubject -> Result.success(electiveSubjectMapper.entityToDTO(electiveSubject)))
                .orElseGet(() -> Result.error("AcademicCurriculumService with ID " + subjectId + " not found"));
    }

    @Override
    @Cacheable(value = "electiveSubjectByNameCache", key = "#name")
    public Result<ElectiveSubjectDTO> getSubjectByName(String name) {
        Optional<ElectiveSubject> optionalSubject = electiveSubjectRepository.findByName(name);
        return optionalSubject.map(electiveSubject -> Result.success(electiveSubjectMapper.entityToDTO(electiveSubject)))
                .orElseGet(() -> Result.error("AcademicCurriculumService with name " + name + " not found"));
    }

    @Override
    @Cacheable(value = "electiveSubjectsByFilterCache", key = "#filterId + '_' + #filterType")
    public Page<ElectiveSubjectDTO> getSubjectsByFilterPageable(Long filterId, String filterType, Pageable pageable) {
        return switch (filterType) {
            case "professionalLine" -> electiveSubjectRepository.findByProfessionalLine(filterId, pageable)
                    .map(electiveSubjectMapper::entityToDTO);
            case "area" -> electiveSubjectRepository.findByAreaId(filterId, pageable)
                    .map(electiveSubjectMapper::entityToDTO);
            case "career" -> electiveSubjectRepository.findByCareerId(filterId, pageable)
                    .map(electiveSubjectMapper::entityToDTO);
            default -> throw new IllegalArgumentException("Invalid filter type: " + filterType);
        };
    }

    @Override
    @Cacheable(value = "allElectiveSubjectsCache")
    public Page<ElectiveSubjectDTO> getAllSubjectsPageable(Pageable pageable) {
        return electiveSubjectRepository.findAll(pageable).map(electiveSubjectMapper::entityToDTO);
    }

    @Override
    public List<ElectiveSubjectDTO> getSubjectsByFilter(Long filterId, String filterType) {
            return switch (filterType) {
                case "career" -> electiveSubjectRepository.findByCareerId(filterId).stream()
                        .map(electiveSubjectMapper::entityToDTO)
                        .toList();
                default -> throw new IllegalArgumentException("Invalid filter type: " + filterType);
            };
    }

    @Override
    public Result<List<ElectiveSubjectDTO>> getSubjectByIdsIn(Set<Long> providedIds) {
            List<Long> missingIds;

            List<ElectiveSubject> electiveSubjects = electiveSubjectRepository.findByIdIn(providedIds);

            List<Long> foundIds = electiveSubjects.stream()
                    .map(ElectiveSubject::getId)
                    .toList();

            missingIds = providedIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            if (!missingIds.isEmpty()) {
                return Result.error("Some IDs were not found:" + missingIds.toString());
            }

            List<ElectiveSubjectDTO> electiveSubjectDTOS = electiveSubjects.stream()
                    .map(electiveSubjectMapper::entityToDTO)
                    .toList();

            return Result.success(electiveSubjectDTOS);
    }

    @Override
    public void createSubject(ElectiveSubjectInsertDTO electiveSubjectInsertDTO) {
        ElectiveSubject electiveSubject = electiveSubjectMapper.insertDtoToEntity(electiveSubjectInsertDTO);

        handleElectiveSubjectRelationships(electiveSubject, electiveSubjectInsertDTO);
        electiveSubjectRepository.saveAndFlush(electiveSubject);

        String key = keyGenerationService.generateSubjectKey(electiveSubject);
        electiveSubject.setKey(key);

        electiveSubjectRepository.save(electiveSubject);
    }

    @Override
    public void updateSubject(ElectiveSubjectInsertDTO electiveSubjectInsertDTO, Long subjectId) {
        ElectiveSubject electiveSubject = electiveSubjectMapper.updateDtoToEntity(electiveSubjectInsertDTO, subjectId);
        electiveSubjectRepository.save(electiveSubject);
    }

    @Override
    public void deleteSubject(Long subjectId) {
        if (!electiveSubjectRepository.existsById(subjectId)) {
            throw new EntityNotFoundException("AcademicCurriculumService with ID" + subjectId + " not found");
        }
        electiveSubjectRepository.deleteById(subjectId);
    }

    private void handleElectiveSubjectRelationships(ElectiveSubject subject, ElectiveSubjectInsertDTO electiveSubjectInsertDTO) {
        getAndSetCareer(subject, electiveSubjectInsertDTO.getCareerId());
        // Only Architecture have area and professional Line
        if (Objects.equals(subject.getCareer().getName(), "Architecture")) {
            getAndSetArea(subject, electiveSubjectInsertDTO.getAreaId());
            getAndSetProfessionalLine(subject, electiveSubjectInsertDTO.getProfessionalLineId());
        }
    }

    private void getAndSetCareer(ElectiveSubject subject, Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Career with ID " + careerId + " not found"));
        subject.setCareer(career);
    }

    private void getAndSetArea(ElectiveSubject subject, Long areaId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException("Area with ID " + areaId + " not found"));
        subject.setArea(area);
    }

    private void getAndSetProfessionalLine(ElectiveSubject subject, Long professionalLineId) {
        ProfessionalLine professionalLine = professionalLineRepository.findById(professionalLineId)
                .orElseThrow(() -> new EntityNotFoundException("ProfessionalLine with ID " + professionalLineId + " not found"));
        subject.setProfessionalLine(professionalLine);
    }
}
