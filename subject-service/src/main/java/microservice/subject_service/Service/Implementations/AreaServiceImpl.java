package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Area.AreaDTO;
import microservice.subject_service.DTOs.Area.AreaInsertDTO;
import microservice.subject_service.DTOs.Area.AreaWithRelationsDTO;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.Mappers.AreaMapper;
import microservice.subject_service.Model.Area;

import microservice.subject_service.Repository.AreaRepository;
import microservice.subject_service.Service.AreaService;
import microservice.subject_service.Service.ElectiveSubjectService;
import microservice.subject_service.Service.OrdinarySubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;
    private final AreaMapper areaMapper;
    private final OrdinarySubjectService ordinarySubjectService;
    private final ElectiveSubjectService electiveSubjectService;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository,
                           AreaMapper areaMapper,
                           OrdinarySubjectService ordinarySubjectService,
                           ElectiveSubjectService electiveSubjectService) {
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
        this.ordinarySubjectService = ordinarySubjectService;
        this.electiveSubjectService = electiveSubjectService;
    }

    @Override
    public Result<AreaDTO> getAreaById(Long areaId) {
        Optional<Area> optionalArea = areaRepository.findById(areaId);
        return optionalArea.map(area -> Result.success(areaMapper.entityToDTO(area)))
                .orElseGet(() -> Result.error("Area with ID " + areaId + " not found"));
    }

    @Override
    public AreaWithRelationsDTO getAreaByIdWithSubjects(Long areaId, Pageable pageable) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException("Area with ID " + areaId + " not found"));

        AreaWithRelationsDTO areaWithRelationsDTO = areaMapper.entityToDTOWithRelations(area);
        return fetchSubjectsAsync(areaWithRelationsDTO, areaId, pageable).join();
    }

    @Override
    public Result<AreaDTO> getAreaByName(String name) {
        Optional<Area> optionalArea = areaRepository.findByName(name);
        return optionalArea.map(area -> Result.success(areaMapper.entityToDTO(area)))
                .orElseGet(() -> Result.error("Area with name " + name + " not found"));
    }

    @Override
    public List<AreaDTO> getAllAreas() {
        List<Area> areas = areaRepository.findAll();
        return areas.stream().map(areaMapper::entityToDTO).toList();
    }

    @Override
    @Transactional
    public void createArea(AreaInsertDTO areaInsertDTO) {
        Area area = areaMapper.insertDtoToEntity(areaInsertDTO);
        areaRepository.save(area);
    }

    @Override
    @Transactional
    public void updateAreaName(AreaInsertDTO areaInsertDTO, Long areaId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException("Area with ID " + areaId + " not found"));

        area.updateName(areaInsertDTO.getName());
        areaRepository.save(area);
    }

    @Override
    @Transactional
    public void deleteArea(Long areaId) {
       if (!areaRepository.existsById(areaId)) {
           throw new EntityNotFoundException("Area with ID " + areaId + " not found");
       }
       areaRepository.deleteById(areaId);
    }

    @Async("TaskExecutor")
    private CompletableFuture<AreaWithRelationsDTO> fetchSubjectsAsync(AreaWithRelationsDTO areaWithRelationsDTO, Long areaId, Pageable pageable) {
        CompletableFuture<Page<OrdinarySubjectDTO>> ordinarySubjectDTOS =
                CompletableFuture.supplyAsync(() -> ordinarySubjectService.getSubjectByAreaId(areaId, pageable));
        CompletableFuture<Page<ElectiveSubjectDTO>> electiveSubjectDTOS =
                CompletableFuture.supplyAsync(() -> electiveSubjectService.getSubjectByAreaId(areaId, pageable));

        return CompletableFuture.allOf(ordinarySubjectDTOS, electiveSubjectDTOS)
                .thenApply(v -> {
                    areaWithRelationsDTO.setOrdinarySubjects(ordinarySubjectDTOS.join());
                    areaWithRelationsDTO.setElectiveSubjects(electiveSubjectDTOS.join());
                    return areaWithRelationsDTO;
                });
    }
}
