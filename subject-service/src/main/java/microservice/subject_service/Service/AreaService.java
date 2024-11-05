package microservice.subject_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Area.AreaDTO;
import microservice.subject_service.DTOs.Area.AreaInsertDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService {
    Result<AreaDTO> getAreaById(Long areaId);
    Result<AreaDTO> getAreaByIdWithSubjects(Long areaId, Pageable pageable);
    Result<AreaDTO> getAreaByName(String name);
    List<AreaDTO> getAllAreas();
    void createArea(AreaInsertDTO areaInsertDTO);
    void updateAreaName(AreaInsertDTO areaInsertDTO, Long areaId);
    void deleteArea(Long areaId);
}
