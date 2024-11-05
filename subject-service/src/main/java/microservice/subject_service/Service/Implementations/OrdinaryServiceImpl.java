package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectInsertDTO;
import microservice.subject_service.Mappers.OrdinarySubjectMapper;
import microservice.subject_service.Model.OrdinarySubject;
import microservice.subject_service.Repository.OrdinarySubjectRepository;
import microservice.subject_service.Service.OrdinarySubjectService;

import java.util.Optional;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class OrdinaryServiceImpl implements OrdinarySubjectService {

    private final OrdinarySubjectRepository ordinarySubjectRepository;
    private final OrdinarySubjectMapper ordinarySubjectMapper;

    @Autowired
    public OrdinaryServiceImpl(OrdinarySubjectRepository ordinarySubjectRepository,
                               OrdinarySubjectMapper ordinarySubjectMapper) {
        this.ordinarySubjectRepository = ordinarySubjectRepository;
        this.ordinarySubjectMapper = ordinarySubjectMapper;
    }

    @Override
    public Result<OrdinarySubjectDTO> getById(Long subjectId) {
        Optional<OrdinarySubject> optionalSubject = ordinarySubjectRepository.findById(subjectId);
        return optionalSubject.map(ordinarySubject -> Result.success(ordinarySubjectMapper.entityToDTO(ordinarySubject)))
                .orElseGet(() -> Result.error("Subject with ID" + subjectId + " not found"));
    }

    @Override
    public Result<OrdinarySubjectDTO> getByName(String name) {
        Optional<OrdinarySubject> optionalSubject = ordinarySubjectRepository.findByName(name);
        return optionalSubject.map(ordinarySubject -> Result.success(ordinarySubjectMapper.entityToDTO(ordinarySubject)))
                .orElseGet(() -> Result.error("Subject with name" + name + " not found"));
    }

    @Override
    public Page<OrdinarySubjectDTO> getBySemester(int semester, Pageable pageable) {
        Page<OrdinarySubject> ordinarySubjects = ordinarySubjectRepository.findBySemester(semester, pageable);
        return ordinarySubjects.map(ordinarySubjectMapper::entityToDTO);
    }

    @Override
    public Page<OrdinarySubjectDTO> getsById(Long subjectId, Pageable pageable) {
        Page<OrdinarySubject> ordinarySubjects = ordinarySubjectRepository.findByAreaId(subjectId, pageable);
        return ordinarySubjects.map(ordinarySubjectMapper::entityToDTO);
    }

    public Page<OrdinarySubjectDTO> getAllSubjects(Pageable pageable) {
        return ordinarySubjectRepository.findByAll(pageable).map(ordinarySubjectMapper::entityToDTO);
    }

    @Override
    @Transactional
    public void createOrdinarySubject(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO) {
        OrdinarySubject ordinarySubject = ordinarySubjectMapper.insertDtoToEntity(ordinarySubjectInsertDTO);
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
}
