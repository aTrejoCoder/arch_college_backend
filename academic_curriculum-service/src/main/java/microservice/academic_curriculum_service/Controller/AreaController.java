package microservice.academic_curriculum_service.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Area.AreaDTO;
import microservice.common_classes.DTOs.Area.AreaInsertDTO;
import microservice.common_classes.DTOs.Area.AreaWithRelationsDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;

import microservice.academic_curriculum_service.Service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/v1/api/areas")
@Slf4j
public class AreaController {

    private final AreaService areaService;

    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }


    @Operation(summary = "Get Area by ID", description = "Fetches the area with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Area found", content = @Content(schema = @Schema(implementation = AreaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Area not found", content = @Content)
    })
    @GetMapping("/{areaId}")
    public ResponseEntity<ResponseWrapper<AreaDTO>> getAreaById(@PathVariable Long areaId) {
        Result<AreaDTO> areaResult = areaService.getAreaById(areaId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Area", "ID", areaId));
        }
        return ResponseEntity.ok(ResponseWrapper.found(areaResult.getData(),"Area", "ID", areaId));
    }

    @Operation(summary = "Get Area by Name", description = "Fetches the area with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Area found", content = @Content(schema = @Schema(implementation = AreaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Area not found", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<AreaDTO>> getAreaByName(@PathVariable String name) {
        Result<AreaDTO> areaResult = areaService.getAreaByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Area", "name", name));
        }
        return ResponseEntity.ok(ResponseWrapper.found(areaResult.getData(),"Area", "name", name));
    }

    @Operation(summary = "Get Area with Subjects by ID", description = "Fetches the area along with its subjects for the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Area with subjects found", content = @Content(schema = @Schema(implementation = AreaWithRelationsDTO.class)))
    })
    @GetMapping("/{areaId}/with-subjects")
    public ResponseEntity<ResponseWrapper<AreaWithRelationsDTO>> getAreaByIdWithSubject(
            @PathVariable Long areaId,
            @Parameter(description = "Page number for pagination", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination", example = "10") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        AreaWithRelationsDTO areas = areaService.getAreaByIdWithSubjects(areaId, pageable);
        return ResponseEntity.ok(ResponseWrapper.found(areas,"Areas"));
    }

    @Operation(summary = "Get All Areas", description = "Fetches all available areas")
    @ApiResponse(responseCode = "200", description = "Areas found", content = @Content(schema = @Schema(implementation = AreaDTO.class)))
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<AreaDTO>>> getAllAreas() {
        List<AreaDTO> areas = areaService.getAllAreas();

        log.info("fetching all areas");
        return ResponseEntity.ok(ResponseWrapper.found(areas,"Areas"));
    }

    @Operation(summary = "Create Area", description = "Creates a new area")
    @ApiResponse(responseCode = "201", description = "Area created", content = @Content)
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createArea(@Valid @RequestBody AreaInsertDTO areaInsertDTO) {
        areaService.createArea(areaInsertDTO);
        return ResponseEntity.ok(ResponseWrapper.created("Area"));
    }

    @Operation(summary = "Update Area", description = "Updates the name of an existing area")
    @ApiResponse(responseCode = "200", description = "Area updated", content = @Content)
    @PutMapping("/{areaId}")
    public ResponseEntity<ResponseWrapper<Void>> updateArea(
            @Valid @RequestBody AreaInsertDTO areaInsertDTO,
            @PathVariable Long areaId) {
        areaService.updateAreaName(areaInsertDTO, areaId);
        return ResponseEntity.ok(ResponseWrapper.updated(null, "Area"));
    }

    @Operation(summary = "Delete Area by ID", description = "Deletes an area with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Area deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Area not found", content = @Content)
    })
    @DeleteMapping("/{areaId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteAreaById(@PathVariable Long areaId) {
        areaService.deleteArea(areaId);
        return ResponseEntity.ok(ResponseWrapper.deleted("Area"));
    }
}
