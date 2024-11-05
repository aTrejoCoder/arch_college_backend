package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.subject_service.Mappers.ElectiveSubjectMapper;
import microservice.subject_service.Model.ElectiveSubject;
import microservice.subject_service.Repository.ElectiveSubjectRepository;
import microservice.subject_service.Service.ElectiveSubjectService;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class ElectiveSubjectServiceImpl implements ElectiveSubjectService {

    private final ElectiveSubjectRepository electiveSubjectRepository;
    private final ElectiveSubjectMapper electiveSubjectMapper;

    @Autowired
    public ElectiveSubjectServiceImpl(ElectiveSubjectRepository electiveSubjectRepository,
                                      ElectiveSubjectMapper electiveSubjectMapper) {
        this.electiveSubjectRepository = electiveSubjectRepository;
        this.electiveSubjectMapper = electiveSubjectMapper;
    }

    @Override
    public Result<ElectiveSubjectDTO> getById(Long subjectId) {
        Optional<ElectiveSubject> optionalSubject = electiveSubjectRepository.findById(subjectId);
        return optionalSubject.map(electiveSubject -> Result.success(electiveSubjectMapper.entityToDTO(electiveSubject)))
                .orElseGet(() -> Result.error("Subject with ID" + subjectId + " not found"));
    }

    @Override
    public Result<ElectiveSubjectDTO> getByName(String name) {
        Optional<ElectiveSubject> optionalSubject = electiveSubjectRepository.findByName(name);
        return optionalSubject.map(electiveSubject -> Result.success(electiveSubjectMapper.entityToDTO(electiveSubject)))
                .orElseGet(() -> Result.error("Subject with name" + name + " not found"));
    }

    @Override
    public Page<ElectiveSubjectDTO> getByProfessionalLineId(Long professionalLineId, Pageable pageable) {
        Page<ElectiveSubject> electiveSubjects = electiveSubjectRepository.findByProfessionalLine(professionalLineId, pageable);
        return electiveSubjects.map(electiveSubjectMapper::entityToDTO);
    }


    @Override
    public Page<ElectiveSubjectDTO> getByAreaId(Long subjectId, Pageable pageable) {
        Page<ElectiveSubject> electiveSubjects = electiveSubjectRepository.findByAreaId(subjectId, pageable);
        return electiveSubjects.map(electiveSubjectMapper::entityToDTO);
    }

    @Override
    public Page<ElectiveSubjectDTO> getAll(Pageable pageable) {
        return electiveSubjectRepository.findByAll(pageable).map(electiveSubjectMapper::entityToDTO);
    }

    @Override
    public void createElectiveSubject(ElectiveSubjectInsertDTO electiveSubjectInsertDTO) {
        ElectiveSubject electiveSubject = electiveSubjectMapper.insertDtoToEntity(electiveSubjectInsertDTO);
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
}
