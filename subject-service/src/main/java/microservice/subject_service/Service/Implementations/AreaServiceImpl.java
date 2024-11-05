package microservice.subject_service.Service.Implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Area.AreaDTO;
import microservice.subject_service.DTOs.Area.AreaInsertDTO;
import microservice.subject_service.Mappers.AreaMapper;
import microservice.subject_service.Model.Area;

import microservice.subject_service.Repository.AreaRepository;
import microservice.subject_service.Repository.ElectiveSubjectRepository;
import microservice.subject_service.Repository.OrdinarySubjectRepository;
import microservice.subject_service.Service.AreaService;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class AreaServiceImpl implements AreaService {
    public final AreaRepository areaRepository;
    public final AreaMapper areaMapper;
    public final OrdinarySubjectRepository ordinarySubjectRepository;
    public final ElectiveSubjectRepository electiveSubjectRepository;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository,
                           AreaMapper areaMapper,
                           OrdinarySubjectRepository ordinarySubjectRepository,
                           ElectiveSubjectRepository electiveSubjectRepository,
                           ElectiveSubjectRepository electiveSubjectRepository1) {
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
        this.ordinarySubjectRepository = ordinarySubjectRepository;
        this.electiveSubjectRepository = electiveSubjectRepository1;
    }

    @Override
    public Result<AreaDTO> getAreaById(Long areaId) {
        Optional<Area> optionalArea = areaRepository.findById(areaId);
        return optionalArea.map(area -> Result.success(areaMapper.entityToDTO(area)))
                .orElseGet(() -> Result.error("Area with ID " + areaId + " not found"));
    }

    @Override
    public Result<AreaDTO> getAreaByIdWithSubjects(Long areaId, Pageable pageable) {
        Optional<Area> optionalArea = areaRepository.findById(areaId);
        if (optionalArea.isEmpty()) {
             return Result.error("Area with ID " + areaId + " not found");
        }
        Area area = optionalArea.get();
        return Result.success(areaMapper.entityToDTO(area));
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
       if (areaRepository.existsById(areaId)) {
           throw new EntityNotFoundException("Area with ID " + areaId + " not found");
       }
       areaRepository.deleteById(areaId);
    }
}
