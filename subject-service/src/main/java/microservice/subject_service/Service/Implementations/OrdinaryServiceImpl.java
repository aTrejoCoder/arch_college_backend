package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectInsertDTO;
import microservice.subject_service.Mappers.OrdinarySubjectMapper;
import microservice.subject_service.Model.Area;
import microservice.subject_service.Model.Career;
import microservice.subject_service.Model.OrdinarySubject;
import microservice.subject_service.Repository.AreaRepository;
import microservice.subject_service.Repository.CareerRepository;
import microservice.subject_service.Repository.OrdinarySubjectRepository;
import microservice.subject_service.Service.OrdinarySubjectService;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class OrdinaryServiceImpl implements OrdinarySubjectService {

    private final OrdinarySubjectRepository ordinarySubjectRepository;
    private final OrdinarySubjectMapper ordinarySubjectMapper;
    private final CareerRepository careerRepository;
    private final AreaRepository areaRepository;

    @Autowired
    public OrdinaryServiceImpl(OrdinarySubjectRepository ordinarySubjectRepository,
                               OrdinarySubjectMapper ordinarySubjectMapper,
                               CareerRepository careerRepository,
                               AreaRepository areaRepository) {
        this.ordinarySubjectRepository = ordinarySubjectRepository;
        this.ordinarySubjectMapper = ordinarySubjectMapper;
        this.careerRepository = careerRepository;
        this.areaRepository = areaRepository;
    }

    @Override
    @Cacheable(value = "ordinarySubjectByIdCache", key = "#subjectId")
    public Result<OrdinarySubjectDTO> getSubjectById(Long subjectId) {
        Optional<OrdinarySubject> optionalSubject = ordinarySubjectRepository.findById(subjectId);
        return optionalSubject.map(subject -> Result.success(ordinarySubjectMapper.entityToDTO(subject)))
                .orElseGet(() -> Result.error("Subject with ID " + subjectId + " not found"));
    }

    @Override
    @Cacheable(value = "ordinarySubjectsByAreaIdCache", key = "#areaId")
    public Page<OrdinarySubjectDTO> getSubjectByAreaId(Long areaId, Pageable pageable) {
        Page<OrdinarySubject> subjects = ordinarySubjectRepository.findByAreaId(areaId, pageable);
        return subjects.map(ordinarySubjectMapper::entityToDTO);
    }

    @Override
    @Cacheable(value = "ordinarySubjectByNameCache", key = "#name")
    public Result<OrdinarySubjectDTO> getSubjectByName(String name) {
        Optional<OrdinarySubject> optionalSubject = ordinarySubjectRepository.findByName(name);
        return optionalSubject.map(subject -> Result.success(ordinarySubjectMapper.entityToDTO(subject)))
                .orElseGet(() -> Result.error("Subject with name " + name + " not found"));
    }

    @Override
    @Cacheable(value = "ordinarySubjectsBySemesterCache", key = "#semester")
    public Page<OrdinarySubjectDTO> getSubjectBySemester(int semester, Pageable pageable) {
        Page<OrdinarySubject> subjects = ordinarySubjectRepository.findBySemester(semester, pageable);
        return subjects.map(ordinarySubjectMapper::entityToDTO);
    }

    @Override
    @Cacheable(value = "allOrdinarySubjectsCache")
    public Page<OrdinarySubjectDTO> getAllSubjects(Pageable pageable) {
        return ordinarySubjectRepository.findAll(pageable).map(ordinarySubjectMapper::entityToDTO);
    }

    @Override
    @Transactional
    public void createOrdinarySubject(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO) {
        OrdinarySubject ordinarySubject = ordinarySubjectMapper.insertDtoToEntity(ordinarySubjectInsertDTO);

        handleOrdinarySubjectRelationships(ordinarySubject, ordinarySubjectInsertDTO);

        ordinarySubjectRepository.saveAndFlush(ordinarySubject);
        ordinarySubject.setKey(generateSubjectKey(ordinarySubject));

        ordinarySubjectRepository.save(ordinarySubject);
    }

    @Override
    @Transactional
    public void updateOrdinarySubject(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO, Long subjectId) {
        OrdinarySubject ordinarySubject = ordinarySubjectMapper.updateDtoToEntity(ordinarySubjectInsertDTO, subjectId);
        ordinarySubjectRepository.save(ordinarySubject);
    }

    @Override
    @Transactional
    public void deleteOrdinarySubject(Long subjectId) {
        if (!ordinarySubjectRepository.existsById(subjectId)) {
            throw new EntityNotFoundException("Subject with ID" + subjectId + " not found");
        }
        ordinarySubjectRepository.deleteById(subjectId);
    }

    /*
    Key for Ordinary Subjects will have 4 numbers --> example : 1203
     (1) means the id of career (architecture careers can't grow above 10, there just 4 in current system and can't get deleted)
     (2) means the number of the semester that the subject belongs (10 semester subject will be considered as 0)
     (03) means the number of subject from some semester
     */
    private String generateSubjectKey(OrdinarySubject subject) {
        int careerIdLastDigit = Math.toIntExact(subject.getCareer().getId());
        int semesterNumber = subject.getSemester() <= 9 ? subject.getSemester() : 0;
        int subjectSequence = ordinarySubjectRepository.findBySemester(subject.getSemester()).size() + 1;

        return String.format("%d%d%02d", careerIdLastDigit, semesterNumber, subjectSequence);
    }

    private void handleOrdinarySubjectRelationships(OrdinarySubject subject, OrdinarySubjectInsertDTO ordinarySubjectInsertDTO) {
        getAndSetCareer(subject, ordinarySubjectInsertDTO.getCareerId());
        getAndSetArea(subject, ordinarySubjectInsertDTO.getAreaId());
    }

    private void getAndSetCareer(OrdinarySubject subject, Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Career with ID " + careerId + " not found"));
        subject.setCareer(career);
    }

    private void getAndSetArea(OrdinarySubject subject, Long areaId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException("Area with ID " + areaId + " not found"));
        subject.setArea(area);
    }

}
