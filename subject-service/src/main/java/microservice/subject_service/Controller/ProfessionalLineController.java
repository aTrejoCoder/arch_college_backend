package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.subject_service.Service.ProfessionalLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/api/professional_lines")
public class ProfessionalLineController {

    private final ProfessionalLineService areaService;

    @Autowired
    public ProfessionalLineController(ProfessionalLineService areaService) {
        this.areaService = areaService;
    }

    @Operation(summary = "Get Professional Line by ID", description = "Fetches a professional line by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professional Line found", content = @Content(schema = @Schema(implementation = ProfessionalLineDTO.class))),
            @ApiResponse(responseCode = "404", description = "Professional Line not found", content = @Content)
    })
    @GetMapping("/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<ProfessionalLineDTO>> getProfessionalLineById(@PathVariable Long professionalLineId) {
        Result<ProfessionalLineDTO> areaResult = areaService.getProfessionalLineById(professionalLineId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Professional Line", "ID", professionalLineId));
        }

        return ResponseEntity.ok(ResponseWrapper.found(areaResult.getData(),"Professional Line", "ID", professionalLineId));
    }

    @Operation(summary = "Get Professional Line by Name", description = "Fetches a professional line by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professional Line found", content = @Content(schema = @Schema(implementation = ProfessionalLineDTO.class))),
            @ApiResponse(responseCode = "404", description = "Professional Line not found", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<ProfessionalLineDTO>> getProfessionalLineByName(@PathVariable String name) {
        Result<ProfessionalLineDTO> areaResult = areaService.getProfessionalLineByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Professional Line", "name", name));
        }

        return ResponseEntity.ok(ResponseWrapper.found(areaResult.getData(),"Professional Line", "name", name));
    }

    @Operation(summary = "Get All Professional Lines", description = "Fetches all professional lines")
    @ApiResponse(responseCode = "200", description = "Professional Lines found", content = @Content(schema = @Schema(implementation = List.class)))
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<ProfessionalLineDTO>>> getAllProfessionalLines() {
        List<ProfessionalLineDTO> professionalLines = areaService.getAllProfessionalLines();

        return ResponseEntity.ok(ResponseWrapper.found(professionalLines, "Professional Lines"));
    }

    @Operation(summary = "Create Professional Line", description = "Creates a new professional line")
    @ApiResponse(responseCode = "201", description = "Professional Line created", content = @Content)
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createProfessionalLine(@Valid @RequestBody ProfessionalLineInsertDTO areaInsertDTO) {
        areaService.createProfessionalLine(areaInsertDTO);

        return ResponseEntity.ok(ResponseWrapper.created(null, "Professional Line"));
    }

    @Operation(summary = "Update Professional Line", description = "Updates an existing professional line by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professional Line updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Professional Line not found", content = @Content)
    })
    @PutMapping("/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<Void>> updateProfessionalLine(@Valid @RequestBody ProfessionalLineInsertDTO areaInsertDTO,
                                                                        @PathVariable Long professionalLineId) {
        areaService.updateProfessionalLineName(areaInsertDTO, professionalLineId);

        return ResponseEntity.ok(ResponseWrapper.updated(null, "Professional Line"));
    }

    @Operation(summary = "Delete Professional Line", description = "Deletes a professional line by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professional Line deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Professional Line not found", content = @Content)
    })
    @DeleteMapping("/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteProfessionalLineById(@PathVariable Long professionalLineId) {
        areaService.deleteProfessionalLine(professionalLineId);

        return ResponseEntity.ok(ResponseWrapper.deleted(null, "Professional Line"));
    }
}
