package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectInsertDTO;
import microservice.subject_service.Service.OrdinarySubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

@RestController
@RequestMapping("/v1/api/subjects/ordinaries")
public class OrdinarySubjectController {

    private final OrdinarySubjectService ordinarySubjectService;

    @Autowired
    public OrdinarySubjectController(OrdinarySubjectService ordinarySubjectService) {
        this.ordinarySubjectService = ordinarySubjectService;
    }

    @Operation(summary = "Get Ordinary Subject by ID", description = "Fetches an ordinary subject by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordinary Subject found", content = @Content(schema = @Schema(implementation = OrdinarySubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ordinary Subject not found", content = @Content)
    })
    @GetMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<OrdinarySubjectDTO>> getOrdinarySubjectById(@PathVariable Long subjectId) {
        Result<OrdinarySubjectDTO> areaResult = ordinarySubjectService.getSubjectById(subjectId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Ordinary Subject", "ID", subjectId));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subject", "ID", subjectId, areaResult.getData()));
    }

    @Operation(summary = "Get Ordinary Subjects by Area ID", description = "Fetches ordinary subjects by area ID")
    @ApiResponse(responseCode = "200", description = "Ordinary Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/by-area/{areaId}")
    public ResponseEntity<ResponseWrapper<Page<OrdinarySubjectDTO>>> getOrdinarySubjectsByAreaId(@PathVariable Long areaId,
                                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdinarySubjectDTO> ordinarySubjectDTOS = ordinarySubjectService.getSubjectByAreaId(areaId, pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subject", "Area Id", areaId, ordinarySubjectDTOS));
    }

    @Operation(summary = "Get Ordinary Subjects by Semester", description = "Fetches ordinary subjects by semester")
    @ApiResponse(responseCode = "200", description = "Ordinary Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/by-semester/{semester}")
    public ResponseEntity<ResponseWrapper<Page<OrdinarySubjectDTO>>> getOrdinarySubjectById(@PathVariable int semester,
                                                                                            @RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdinarySubjectDTO> ordinarySubjectDTOS = ordinarySubjectService.getSubjectBySemester(semester, pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subject", "Semester", semester, ordinarySubjectDTOS));
    }

    @Operation(summary = "Get Ordinary Subject by Name", description = "Fetches an ordinary subject by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordinary Subject found", content = @Content(schema = @Schema(implementation = OrdinarySubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ordinary Subject not found", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<OrdinarySubjectDTO>> getOrdinarySubjectByName(@PathVariable String name) {
        Result<OrdinarySubjectDTO> areaResult = ordinarySubjectService.getSubjectByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("OrdinarySubject", "name", name));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subject", "name", name, areaResult.getData()));
    }

    @Operation(summary = "Get All Ordinary Subjects", description = "Fetches all ordinary subjects with pagination")
    @ApiResponse(responseCode = "200", description = "Ordinary Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<Page<OrdinarySubjectDTO>>> getAllOrdinarySubjects(@RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdinarySubjectDTO> professionalLines = ordinarySubjectService.getAllSubjects(pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subjects", professionalLines));
    }

    @Operation(summary = "Create Ordinary Subject", description = "Creates a new ordinary subject")
    @ApiResponse(responseCode = "201", description = "Ordinary Subject created", content = @Content)
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createOrdinarySubject(@Valid @RequestBody OrdinarySubjectInsertDTO areaInsertDTO) {
        ordinarySubjectService.createOrdinarySubject(areaInsertDTO);

        return ResponseEntity.ok(ResponseWrapper.created(null,"Ordinary Subject"));
    }

    @Operation(summary = "Update Ordinary Subject", description = "Updates an existing ordinary subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordinary Subject updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ordinary Subject not found", content = @Content)
    })
    @PutMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> updateOrdinarySubject(@Valid @RequestBody OrdinarySubjectInsertDTO areaInsertDTO,
                                                                       @PathVariable Long subjectId) {
        ordinarySubjectService.updateOrdinarySubject(areaInsertDTO, subjectId);
        return ResponseEntity.ok(ResponseWrapper.updated(null,"Ordinary Subject"));
    }

    @Operation(summary = "Delete Ordinary Subject", description = "Deletes an ordinary subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordinary Subject deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ordinary Subject not found", content = @Content)
    })
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteOrdinarySubjectById(@PathVariable Long subjectId) {
        ordinarySubjectService.deleteOrdinarySubject(subjectId);
        return ResponseEntity.ok(ResponseWrapper.deleted(null,"Ordinary Subject"));
    }
}
