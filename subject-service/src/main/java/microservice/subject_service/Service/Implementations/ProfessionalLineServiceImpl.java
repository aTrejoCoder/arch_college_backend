package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.subject_service.Mappers.ProfessionalLineMapper;
import microservice.subject_service.Model.Area;
import microservice.subject_service.Model.ProfessionalLine;
import microservice.subject_service.Repository.AreaRepository;
import microservice.subject_service.Repository.ProfessionalLineRepository;
import microservice.subject_service.Service.ProfessionalLineService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalLineServiceImpl implements ProfessionalLineService {
    private final ProfessionalLineMapper professionalLineMapper;
    private final AreaRepository areaRepository;
    private final ProfessionalLineRepository professionalLineRepository;


    @Autowired
    public ProfessionalLineServiceImpl(ProfessionalLineMapper professionalLineMapper,
                                       AreaRepository areaRepository,
                                       ProfessionalLineRepository professionalLineRepository) {
        this.professionalLineMapper = professionalLineMapper;
        this.areaRepository = areaRepository;
        this.professionalLineRepository = professionalLineRepository;
    }

    @Override
    @Cacheable(value = "professionalLineByIdCache", key = "#professionalLineId")
    public Result<ProfessionalLineDTO> getProfessionalLineById(Long professionalLineId) {
        Optional<ProfessionalLine> optionalProfessionalLine = professionalLineRepository.findById(professionalLineId);
        return optionalProfessionalLine.map(professionalLine -> Result.success(professionalLineMapper.entityToDTO(professionalLine)))
                .orElseGet(() -> Result.error("Professional Line with ID " + professionalLineId + " not found"));
    }

    @Override
    @Cacheable(value = "professionalLineWithSubjectsCache", key = "#professionalLineId")
    public Result<ProfessionalLineDTO> getProfessionalLineByIdWithSubjects(Long professionalLineId) {
        Optional<ProfessionalLine> optionalProfessionalLine = professionalLineRepository.findById(professionalLineId);
        if (optionalProfessionalLine.isEmpty()) {
            return Result.error("Professional Line with ID " + professionalLineId + " not found");
        }
        ProfessionalLine professionalLine = optionalProfessionalLine.get();
        return Result.success(professionalLineMapper.entityToDTO(professionalLine));
    }

    @Override
    @Cacheable(value = "professionalLineByNameCache", key = "#name")
    public Result<ProfessionalLineDTO> getProfessionalLineByName(String name) {
        Optional<ProfessionalLine> optionalProfessionalLine = professionalLineRepository.findByName(name);
        return optionalProfessionalLine.map(professionalLine -> Result.success(professionalLineMapper.entityToDTO(professionalLine)))
                .orElseGet(() -> Result.error("Professional Line with name " + name + " not found"));
    }

    @Override
    @Cacheable(value = "allProfessionalLinesCache")
    public List<ProfessionalLineDTO> getAllProfessionalLines() {
        return professionalLineRepository.findAll().stream().map(professionalLineMapper::entityToDTO).toList();
    }
    @Override
    @Transactional
    public void createProfessionalLine(ProfessionalLineInsertDTO professionalLineInsertDTO) {
        ProfessionalLine professionalLine = professionalLineMapper.insertDtoToEntity(professionalLineInsertDTO);

        getAndSetArea(professionalLine, professionalLineInsertDTO.getAreaId());

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

    private void getAndSetArea(ProfessionalLine professionalLine, Long areaId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow (() -> new RuntimeException("Area is not available"));
        professionalLine.setArea(area);
    }
}
