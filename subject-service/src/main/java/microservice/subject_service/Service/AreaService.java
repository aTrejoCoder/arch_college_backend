package microservice.subject_service.Service;

import microservice.common_classes.DTOs.Area.AreaDTO;
import microservice.common_classes.DTOs.Area.AreaInsertDTO;
import microservice.common_classes.DTOs.Area.AreaWithRelationsDTO;
import microservice.common_classes.Utils.Result;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService {
    Result<AreaDTO> getAreaById(Long areaId);
    AreaWithRelationsDTO getAreaByIdWithSubjects(Long areaId, Pageable pageable);
    Result<AreaDTO> getAreaByName(String name);
    List<AreaDTO> getAllAreas();
    void createArea(AreaInsertDTO areaInsertDTO);
    void updateAreaName(AreaInsertDTO areaInsertDTO, Long areaId);
    void deleteArea(Long areaId);
}
