package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.subject_service.Service.ElectiveSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/api/subjects/electives")
public class ElectiveSubjectController {

    private final ElectiveSubjectService electiveSubjectService;

    @Autowired
    public ElectiveSubjectController(ElectiveSubjectService electiveSubjectService) {
        this.electiveSubjectService = electiveSubjectService;
    }

    @Operation(summary = "Get Elective Subject by ID", description = "Fetches an elective subject by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective Subject found", content = @Content(schema = @Schema(implementation = ElectiveSubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Elective Subject not found", content = @Content)
    })
    @GetMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> getElectiveSubjectById(@PathVariable Long subjectId) {
        Result<ElectiveSubjectDTO> areaResult = electiveSubjectService.getSubjectById(subjectId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Elective Subject", "ID", subjectId));
        }
        return ResponseEntity.ok(ResponseWrapper.found("Elective Subject", "ID", subjectId, areaResult.getData()));
    }

    @Operation(summary = "Get Elective Subjects by Area ID", description = "Fetches elective subjects by area ID")
    @ApiResponse(responseCode = "200", description = "Elective Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("by-area/{areaId}")
    public ResponseEntity<ResponseWrapper<Page<ElectiveSubjectDTO>>> getElectiveSubjectByAreaId(
            @PathVariable Long areaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ElectiveSubjectDTO> electiveSubjectDTOS = electiveSubjectService.getSubjectByAreaId(areaId, pageable);
        return ResponseEntity.ok(ResponseWrapper.found("Elective Subject", "Area ID", areaId, electiveSubjectDTOS));
    }

    @Operation(summary = "Get Elective Subjects by Professional Line ID", description = "Fetches elective subjects by professional line ID")
    @ApiResponse(responseCode = "200", description = "Elective Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("by-professional_line/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<Page<ElectiveSubjectDTO>>> getElectiveSubjectByProfessionalLineId(
            @PathVariable Long professionalLineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ElectiveSubjectDTO> electiveSubjectDTOS = electiveSubjectService.getSubjectByProfessionalLineId(professionalLineId, pageable);
        return ResponseEntity.ok(ResponseWrapper.found("Elective Subject", "Professional Line ID", professionalLineId, electiveSubjectDTOS));
    }

    @Operation(summary = "Get Elective Subject by Name", description = "Fetches an elective subject by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective Subject found", content = @Content(schema = @Schema(implementation = ElectiveSubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Elective Subject not found", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> getElectiveSubjectByName(@PathVariable String name) {
        Result<ElectiveSubjectDTO> areaResult = electiveSubjectService.getSubjectByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("ElectiveSubject", "name", name));
        }
        return ResponseEntity.ok(ResponseWrapper.found("Elective Subject", "name", name, areaResult.getData()));
    }

    @Operation(summary = "Get All Elective Subjects", description = "Fetches all elective subjects with pagination")
    @ApiResponse(responseCode = "200", description = "Elective Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<Page<ElectiveSubjectDTO>>> getAllElectiveSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ElectiveSubjectDTO> professionalLines = electiveSubjectService.getSubjectAll(pageable);
        return ResponseEntity.ok(ResponseWrapper.found("Elective Subjects", professionalLines));
    }

    @Operation(summary = "Create Elective Subject", description = "Creates a new elective subject")
    @ApiResponse(responseCode = "201", description = "Elective Subject created", content = @Content)
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createElectiveSubject(@Valid @RequestBody ElectiveSubjectInsertDTO areaInsertDTO) {
        electiveSubjectService.createElectiveSubject(areaInsertDTO);
        return ResponseEntity.ok(ResponseWrapper.created(null,"Elective Subject"));
    }

    @Operation(summary = "Update Elective Subject", description = "Updates an existing elective subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective Subject updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Elective Subject not found", content = @Content)
    })
    @PutMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> updateElectiveSubject(
            @Valid @RequestBody ElectiveSubjectInsertDTO areaInsertDTO,
            @PathVariable Long subjectId) {
        electiveSubjectService.updateElectiveSubject(areaInsertDTO, subjectId);
        return ResponseEntity.ok(ResponseWrapper.updated(null,"Elective Subject"));
    }

    @Operation(summary = "Delete Elective Subject", description = "Deletes an elective subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective Subject deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Elective Subject not found", content = @Content)
    })
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteElectiveSubjectById(@PathVariable Long subjectId) {
        electiveSubjectService.deleteElectiveSubject(subjectId);
        return ResponseEntity.ok(ResponseWrapper.deleted(null,"Elective Subject"));
    }
}
