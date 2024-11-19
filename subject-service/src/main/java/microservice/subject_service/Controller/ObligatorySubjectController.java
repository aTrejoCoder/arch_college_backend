package microservice.subject_service.Controller;

import jakarta.validation.Valid;

import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectInsertDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.Service.SubjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/api/subjects/obligatory")
public class ObligatorySubjectController {

    private final SubjectService<ObligatorySubjectDTO, ObligatorySubjectInsertDTO> subjectService;

    @Autowired
    public ObligatorySubjectController(SubjectService<ObligatorySubjectDTO, ObligatorySubjectInsertDTO> subjectService) {
        this.subjectService = subjectService;
    }

    @Operation(summary = "Get Obligatory Subject by ID", description = "Fetches an ordinary subject by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obligatory Subject found", content = @Content(schema = @Schema(implementation = ObligatorySubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Obligatory Subject not found", content = @Content)
    })
    @GetMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<ObligatorySubjectDTO>> getObligatorySubjectById(@PathVariable Long subjectId) {
        Result<ObligatorySubjectDTO> areaResult = subjectService.getSubjectById(subjectId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Obligatory Subject", "ID", subjectId));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Obligatory Subject", "ID", subjectId, areaResult.getData()));
    }

    @Operation(summary = "Get Obligatory Subjects by Area ID", description = "Fetches ordinary subjects by area ID")
    @ApiResponse(responseCode = "200", description = "Obligatory Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/by-area/{areaId}")
    public ResponseEntity<ResponseWrapper<Page<ObligatorySubjectDTO>>> getObligatorySubjectsByAreaId(@PathVariable Long areaId,
                                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ObligatorySubjectDTO> ordinarySubjectDTOS = subjectService.getSubjectsByFilterPageable(areaId, "area", pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Obligatory Subject", "Area Id", areaId, ordinarySubjectDTOS));
    }


    @GetMapping("/by-semester/{semester_number}")
    public ResponseEntity<ResponseWrapper<Page<ObligatorySubjectDTO>>> getObligatorySubjectBySemesterNumber(@PathVariable Long semester_number,
                                                                                                          @RequestParam(defaultValue = "0") int page,
                                                                                                          @RequestParam(defaultValue = "10") int size) {

        if (semester_number > 10) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest("10th semester is the last semester"));
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ObligatorySubjectDTO> ordinarySubjectDTOS = subjectService.getSubjectsByFilterPageable(semester_number, "semester",pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Obligatory Subject", "semester", semester_number, ordinarySubjectDTOS));
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<ObligatorySubjectDTO>> getObligatorySubjectByName(@PathVariable String name) {
        Result<ObligatorySubjectDTO> areaResult = subjectService.getSubjectByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("ObligatorySubject", "name", name));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Obligatory Subject", "name", name, areaResult.getData()));
    }

    @Operation(summary = "Get All Obligatory Subjects", description = "Fetches all ordinary subjects with pagination")
    @ApiResponse(responseCode = "200", description = "Obligatory Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<Page<ObligatorySubjectDTO>>> getAllObligatorySubjects(@RequestParam(defaultValue = "0") int page,
                                                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ObligatorySubjectDTO> professionalLines = subjectService.getAllSubjectsPageable(pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Obligatory Subjects", professionalLines));
    }

    @Operation(summary = "Create Obligatory Subject", description = "Creates a new ordinary subject")
    @ApiResponse(responseCode = "201", description = "Obligatory Subject created", content = @Content)
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createObligatorySubject(@Valid @RequestBody ObligatorySubjectInsertDTO areaInsertDTO) {
        subjectService.createSubject(areaInsertDTO);

        return ResponseEntity.ok(ResponseWrapper.created(null,"Obligatory Subject"));
    }

    @Operation(summary = "Update Obligatory Subject", description = "Updates an existing ordinary subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obligatory Subject updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Obligatory Subject not found", content = @Content)
    })
    @PutMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> updateObligatorySubject(@Valid @RequestBody ObligatorySubjectInsertDTO areaInsertDTO,
                                                                       @PathVariable Long subjectId) {
        subjectService.updateSubject(areaInsertDTO, subjectId);
        return ResponseEntity.ok(ResponseWrapper.updated("Obligatory Subject"));
    }

    @Operation(summary = "Delete Obligatory Subject", description = "Deletes an ordinary subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obligatory Subject deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Obligatory Subject not found", content = @Content)
    })
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteObligatorySubjectById(@PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(ResponseWrapper.deleted("Obligatory Subject"));
    }
}
