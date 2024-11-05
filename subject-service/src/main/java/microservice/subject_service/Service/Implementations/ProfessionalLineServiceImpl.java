package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.subject_service.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;
import microservice.subject_service.Mappers.ProfessionalLineMapper;
import microservice.subject_service.Model.ProfessionalLine;
import microservice.subject_service.Repository.ProfessionalLineRepository;
import microservice.subject_service.Service.ProfessionalLineService;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalLineServiceImpl implements ProfessionalLineService {
    private final ProfessionalLineMapper professionalLineMapper;
    private final ProfessionalLineRepository professionalLineRepository;

    @Autowired
    public ProfessionalLineServiceImpl(ProfessionalLineMapper professionalLineMapper,
                                       ProfessionalLineRepository professionalLineRepository) {
        this.professionalLineMapper = professionalLineMapper;
        this.professionalLineRepository = professionalLineRepository;
    }

    @Override
    public Result<ProfessionalLineDTO> getProfessionalLineById(Long professionalLineId) {
        Optional<ProfessionalLine> optionalProfessionalLine = professionalLineRepository.findById(professionalLineId);
        return optionalProfessionalLine.map(professionalLine -> Result.success(professionalLineMapper.entityToDTO(professionalLine)))
                .orElseGet(() -> Result.error("Professional Line with ID " + professionalLineId + " not found"));
    }

    @Override
    public Result<ProfessionalLineDTO> getProfessionalLineByIdWithSubjects(Long professionalLineId) {
        Optional<ProfessionalLine> optionalProfessionalLine = professionalLineRepository.findById(professionalLineId);
        if (optionalProfessionalLine.isEmpty()) {
            return Result.error("Professional Line with ID " + professionalLineId + " not found");
        }
        ProfessionalLine professionalLine = optionalProfessionalLine.get();
        return Result.success(professionalLineMapper.entityToDTO(professionalLine));
    }

    @Override
    public Result<ProfessionalLineDTO> getProfessionalLineByName(String name) {
        Optional<ProfessionalLine> optionalProfessionalLine = professionalLineRepository.findByName(name);
        return optionalProfessionalLine.map(professionalLine -> Result.success(professionalLineMapper.entityToDTO(professionalLine)))
                .orElseGet(() -> Result.error("Professional Line with name " + name + " not found"));
    }

    @Override
    public List<ProfessionalLineDTO> getAllProfessionalLines() {
        return professionalLineRepository.findAll().stream().map(professionalLineMapper::entityToDTO).toList();
    }

    @Override
    @Transactional
    public void createProfessionalLine(ProfessionalLineInsertDTO professionalLineInsertDTO) {
        ProfessionalLine professionalLine = professionalLineMapper.insertDtoToEntity(professionalLineInsertDTO);
        professionalLineRepository.save(professionalLine);
    }

    @Override
    @Transactional
    public void updateProfessionalLineName(ProfessionalLineInsertDTO professionalLineInsertDTO, Long professionalLineId) {
        ProfessionalLine professionalLine = professionalLineRepository.findById(professionalLineId)
                .orElseThrow(() -> new EntityNotFoundException("Professional Line with ID " + professionalLineId + " not found"));

        professionalLine.updateName(professionalLineInsertDTO.getName());
        professionalLineRepository.save(professionalLine);
    }

    @Override
    @Transactional
    public void deleteProfessionalLine(Long professionalLineId) {
        if (!professionalLineRepository.existsById(professionalLineId)) {
            throw new EntityNotFoundException("Professional Line with ID " + professionalLineId + " not found");
        }

        professionalLineRepository.deleteById(professionalLineId);
    }
}
