package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Area.AreaDTO;
import microservice.subject_service.DTOs.Area.AreaInsertDTO;
import microservice.subject_service.DTOs.Area.AreaWithRelationsDTO;
import microservice.subject_service.Service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/areas")
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound( "Area", "ID", areaId));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Area", "ID", areaId, areaResult.getData()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<AreaDTO>> getAreaByName(@PathVariable String name) {
        Result<AreaDTO> areaResult = areaService.getAreaByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Area", "name", name));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Area", "name", name, areaResult.getData()));
    }

    @GetMapping("/{areaId}/with-subjects")
    public ResponseEntity<ResponseWrapper<AreaWithRelationsDTO>> getAreaByIdWithSubject(@PathVariable Long areaId,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        AreaWithRelationsDTO areas = areaService.getAreaByIdWithSubjects(areaId, pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Areas", areas));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<AreaDTO>>> getAllAreas() {
        List<AreaDTO> areas = areaService.getAllAreas();

        return ResponseEntity.ok(ResponseWrapper.found("Areas", areas));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createArea(@Valid @RequestBody AreaInsertDTO areaInsertDTO) {
        areaService.createArea(areaInsertDTO);

        return ResponseEntity.ok(ResponseWrapper.created(null,"Area"));
    }

    @PutMapping("/{areaId}")
    public ResponseEntity<ResponseWrapper<Void>> updateArea(@Valid @RequestBody AreaInsertDTO areaInsertDTO,
                                                            @PathVariable Long areaId) {
        areaService.updateAreaName(areaInsertDTO, areaId);

        return ResponseEntity.ok(ResponseWrapper.updated(null,"Area"));
    }

    @DeleteMapping("/{areaId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteAreaById(@PathVariable Long areaId) {
        areaService.deleteArea(areaId);

        return ResponseEntity.ok(ResponseWrapper.deleted(null,"Area"));
    }
}
