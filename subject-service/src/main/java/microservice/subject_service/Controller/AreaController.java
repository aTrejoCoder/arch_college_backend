package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Area.AreaDTO;
import microservice.subject_service.DTOs.Area.AreaInsertDTO;
import microservice.subject_service.Service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/subjects/areas")
public class AreaController {

    private final AreaService areaService;

    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/{areaId}")
    public ResponseEntity<ResponseWrapper<AreaDTO>> getAreaById(@PathVariable Long areaId) {
        Result<AreaDTO> areaResult = areaService.getAreaById(areaId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(areaResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.buildResponse(
                ResponseWrapper.StatusType.FOUND, "Area", "ID", areaId.toString(), areaResult.getData()

        ));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResponseWrapper<AreaDTO>> getAreaByName(@PathVariable String name) {
        Result<AreaDTO> areaResult = areaService.getAreaByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(areaResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(areaResult.getData(), "Area With name" + name + " successfully fetched"));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<AreaDTO>>> getAllAreas() {
        List<AreaDTO> areas = areaService.getAllAreas();

        return ResponseEntity.ok(ResponseWrapper.ok(areas, "All areas successfully fetched"));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createArea(@Valid @RequestBody AreaInsertDTO areaInsertDTO) {
        areaService.createArea(areaInsertDTO);
        return ResponseEntity.ok(ResponseWrapper.ok(null,"Areas successfully created"));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper<Void>> updateArea(@Valid @RequestBody AreaInsertDTO areaInsertDTO,
                                                            @PathVariable Long areaId) {
        areaService.updateAreaName(areaInsertDTO, areaId);
        return ResponseEntity.ok(ResponseWrapper.ok(null,"Areas successfully updated"));
    }

    @DeleteMapping
    public ResponseEntity<ResponseWrapper<Void>> deleteAreaById(@Valid @RequestBody AreaInsertDTO areaInsertDTO,
                                                            @PathVariable Long areaId) {
        areaService.deleteArea(areaId);
        return ResponseEntity.ok(ResponseWrapper.ok(null,"Areas successfully deleted"));
    }

}
