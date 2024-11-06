package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.subject_service.Mappers.ElectiveSubjectMapper;
import microservice.subject_service.Model.Area;
import microservice.subject_service.Model.Career;
import microservice.subject_service.Model.ElectiveSubject;
import microservice.subject_service.Model.ProfessionalLine;
import microservice.subject_service.Repository.AreaRepository;
import microservice.subject_service.Repository.CareerRepository;
import microservice.subject_service.Repository.ElectiveSubjectRepository;
import microservice.subject_service.Repository.ProfessionalLineRepository;
import microservice.subject_service.Service.ElectiveSubjectService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class ElectiveSubjectServiceImpl implements ElectiveSubjectService {

    private final ElectiveSubjectRepository electiveSubjectRepository;
    private final ElectiveSubjectMapper electiveSubjectMapper;
    private final AreaRepository areaRepository;
    private final CareerRepository careerRepository;
    private final ProfessionalLineRepository professionalLineRepository;

    @Autowired
    public ElectiveSubjectServiceImpl(ElectiveSubjectRepository electiveSubjectRepository,
                                      ElectiveSubjectMapper electiveSubjectMapper,
                                      AreaRepository areaRepository,
                                      CareerRepository careerRepository,
                                      ProfessionalLineRepository professionalLineRepository) {
        this.electiveSubjectRepository = electiveSubjectRepository;
        this.electiveSubjectMapper = electiveSubjectMapper;
        this.areaRepository = areaRepository;
        this.careerRepository = careerRepository;
        this.professionalLineRepository = professionalLineRepository;
    }

    @Override
    public Result<ElectiveSubjectDTO> getSubjectById(Long subjectId) {
        Optional<ElectiveSubject> optionalSubject = electiveSubjectRepository.findById(subjectId);
        return optionalSubject.map(electiveSubject -> Result.success(electiveSubjectMapper.entityToDTO(electiveSubject)))
                .orElseGet(() -> Result.error("Subject with ID" + subjectId + " not found"));
    }

    @Override
    public Result<ElectiveSubjectDTO> getSubjectByName(String name) {
        Optional<ElectiveSubject> optionalSubject = electiveSubjectRepository.findByName(name);
        return optionalSubject.map(electiveSubject -> Result.success(electiveSubjectMapper.entityToDTO(electiveSubject)))
                .orElseGet(() -> Result.error("Subject with name" + name + " not found"));
    }

    @Override
    public Page<ElectiveSubjectDTO> getSubjectByProfessionalLineId(Long professionalLineId, Pageable pageable) {
        Page<ElectiveSubject> electiveSubjects = electiveSubjectRepository.findByProfessionalLine(professionalLineId, pageable);
        return electiveSubjects.map(electiveSubjectMapper::entityToDTO);
    }

    @Override
    public Page<ElectiveSubjectDTO> getSubjectByAreaId(Long subjectId, Pageable pageable) {
        Page<ElectiveSubject> electiveSubjects = electiveSubjectRepository.findByAreaId(subjectId, pageable);
        return electiveSubjects.map(electiveSubjectMapper::entityToDTO);
    }

    @Override
    public Page<ElectiveSubjectDTO> getSubjectAll(Pageable pageable) {
        return electiveSubjectRepository.findAll(pageable).map(electiveSubjectMapper::entityToDTO);
    }

    @Override
    public void createElectiveSubject(ElectiveSubjectInsertDTO electiveSubjectInsertDTO) {
        ElectiveSubject electiveSubject = electiveSubjectMapper.insertDtoToEntity(electiveSubjectInsertDTO);

        handleElectiveSubjectRelationships(electiveSubject, electiveSubjectInsertDTO);
        electiveSubjectRepository.saveAndFlush(electiveSubject);

        electiveSubject.setKey(generateSubjectKey(electiveSubject));
        electiveSubjectRepository.save(electiveSubject);
    }

    @Override
    public void updateElectiveSubject(ElectiveSubjectInsertDTO electiveSubjectInsertDTO, Long subjectId) {
        ElectiveSubject electiveSubject = electiveSubjectMapper.updateDtoToEntity(electiveSubjectInsertDTO, subjectId);
        electiveSubjectRepository.save(electiveSubject);
    }

    @Override
    public void deleteElectiveSubject(Long subjectId) {
        if (!electiveSubjectRepository.existsById(subjectId)) {
            throw new EntityNotFoundException("Subject with ID" + subjectId + " not found");
        }
        electiveSubjectRepository.deleteById(subjectId);
    }


    /*
    Key for Elective Subjects will have 4 numbers --> example : 151
    (1) means the ID from professional line id
    (50) means the number of subject from professional line
 */
    private String generateSubjectKey(ElectiveSubject subject) {
        int professionalLineIdLastDigit = Math.toIntExact(subject.getProfessionalLine().getId());
        int subjectSequence = electiveSubjectRepository.countByProfessionalLineId(subject.getProfessionalLine().getId()) + 1;

        return String.format("%d%02d", professionalLineIdLastDigit, subjectSequence);
    }

    private void handleElectiveSubjectRelationships(ElectiveSubject subject, ElectiveSubjectInsertDTO electiveSubjectInsertDTO) {
        getAndSetCareer(subject, electiveSubjectInsertDTO.getCareerId());
        getAndSetArea(subject, electiveSubjectInsertDTO.getAreaId());
        getAndSetProfessionalLine(subject, electiveSubjectInsertDTO.getProfessionalLineId());
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
