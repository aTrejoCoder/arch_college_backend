package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectInsertDTO;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.Mappers.ObligatorySubjectMapper;
import microservice.subject_service.Model.Area;
import microservice.subject_service.Model.Career;
import microservice.subject_service.Model.ObligatorySubject;
import microservice.subject_service.Repository.AreaRepository;
import microservice.subject_service.Repository.CareerRepository;
import microservice.subject_service.Repository.ObligatorySubjectRepository;

import java.util.Optional;

import microservice.subject_service.Service.SubjectService;
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

    @Autowired
    public ObligatoryServiceImpl(ObligatorySubjectRepository obligatorySubjectRepository,
                               ObligatorySubjectMapper obligatorySubjectMapper,
                               CareerRepository careerRepository,
                               AreaRepository areaRepository) {
        this.obligatorySubjectRepository = obligatorySubjectRepository;
        this.obligatorySubjectMapper = obligatorySubjectMapper;
        this.careerRepository = careerRepository;
        this.areaRepository = areaRepository;
    }

    @Override
    @Cacheable(value = "obligatorySubjectByIdCache", key = "#subjectId")
    public Result<ObligatorySubjectDTO> getSubjectById(Long subjectId) {
        Optional<ObligatorySubject> optionalSubject = obligatorySubjectRepository.findById(subjectId);
        return optionalSubject.map(subject -> Result.success(obligatorySubjectMapper.entityToDTO(subject)))
                .orElseGet(() -> Result.error("Subject with ID " + subjectId + " not found"));
    }


    @Override
    @Cacheable(value = "obligatorySubjectByNameCache", key = "#name")
    public Result<ObligatorySubjectDTO> getSubjectByName(String name) {
        Optional<ObligatorySubject> optionalSubject = obligatorySubjectRepository.findByName(name);
        return optionalSubject.map(subject -> Result.success(obligatorySubjectMapper.entityToDTO(subject)))
                .orElseGet(() -> Result.error("Subject with name " + name + " not found"));
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
    @Transactional
    public void createSubject(ObligatorySubjectInsertDTO obligatorySubjectInsertDTO) {
        ObligatorySubject obligatorySubject = obligatorySubjectMapper.insertDtoToEntity(obligatorySubjectInsertDTO);

        handleObligatorySubjectRelationships(obligatorySubject, obligatorySubjectInsertDTO);

        obligatorySubjectRepository.saveAndFlush(obligatorySubject);
        obligatorySubject.setKey(generateSubjectKey(obligatorySubject));

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
            throw new EntityNotFoundException("Subject with ID" + subjectId + " not found");
        }
        obligatorySubjectRepository.deleteById(subjectId);
    }

    /*
    Key for Obligatory Subjects will have 4 numbers --> example : 1203
     (1) means the id of career (architecture careers can't grow above 10, there just 4 in current system and can't get deleted)
     (2) means the number of the semester that the subject belongs (10 semester subject will be considered as 0)
     (03) means the number of subject from some semester
     */
    private String generateSubjectKey(ObligatorySubject subject) {
        int careerIdLastDigit = Math.toIntExact(subject.getCareer().getId());
        int semesterNumber = subject.getSemester() <= 9 ? subject.getSemester() : 0;
        int subjectSequence = obligatorySubjectRepository.findBySemester(subject.getSemester()).size();

        return String.format("%d%d%02d", careerIdLastDigit, semesterNumber, subjectSequence);
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
