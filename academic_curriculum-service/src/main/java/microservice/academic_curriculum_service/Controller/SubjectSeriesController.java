package microservice.academic_curriculum_service.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import microservice.common_classes.DTOs.Subject.SubjectSeriesDTO;
import microservice.academic_curriculum_service.DTOs.SocialNetwork.SubjectSeriesInsertDTO;
import microservice.academic_curriculum_service.Service.SubjectSeriesService;
import microservice.academic_curriculum_service.Service.SubjectService;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectInsertDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("v1/api/academic_curriculum/subject-series")
@RequiredArgsConstructor
@Tag(name = "Subject Series", description = "Endpoints for managing subject series")
public class SubjectSeriesController {

    private final SubjectSeriesService subjectSeriesService;
    private final SubjectService<ObligatorySubjectDTO, ObligatorySubjectInsertDTO> obligatorySubjectService;
    private final SubjectService<ElectiveSubjectDTO, ElectiveSubjectInsertDTO> electiveSubjectService;

    @GetMapping("/obligatory/{obligatorySubjectId}")
    @Operation(summary = "Get subject series by obligatory subject ID", description = "Fetches a subject series based on the given obligatory subject ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject series found", content = @Content(schema = @Schema(implementation = SubjectSeriesDTO.class))),
            @ApiResponse(responseCode = "404", description = "Subject series not found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper<SubjectSeriesDTO>> getSeriesByObligatorySubjectId(@PathVariable Long obligatorySubjectId) {
        SubjectSeriesDTO subjectSeriesDTO = subjectSeriesService.getSubjectSeriesByObligatorySubjectId(obligatorySubjectId);

        if (subjectSeriesDTO == null) {
            return ResponseEntity.ok(ResponseWrapper.ok("Obligatory Subject With ID " + obligatorySubjectId + " Doesn't Have Serialization"));
        }

        return ResponseEntity.ok(ResponseWrapper.found(subjectSeriesDTO, "Subject Series"));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all subject series", description = "Retrieves a paginated list of all subject series.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject series found", content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<ResponseWrapper<Page<SubjectSeriesDTO>>> getAllSubjectSeries(@RequestParam(defaultValue = "0") int page,
                                                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SubjectSeriesDTO> subjectSeriesDTOS = subjectSeriesService.getAll(pageable);

        return ResponseEntity.ok(ResponseWrapper.found(subjectSeriesDTOS, "Subject Series"));
    }

    @GetMapping("/elective/{electiveSubjectId}")
    @Operation(summary = "Get subject series by elective subject ID", description = "Fetches a subject series based on the given elective subject ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject series found", content = @Content(schema = @Schema(implementation = SubjectSeriesDTO.class))),
            @ApiResponse(responseCode = "404", description = "Subject series not found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper<SubjectSeriesDTO>> getSeriesByElectiveSubjectId(@PathVariable Long electiveSubjectId) {
        SubjectSeriesDTO subjectSeriesDTO = subjectSeriesService.getSubjectSeriesByElectiveSubjectId(electiveSubjectId);

        if (subjectSeriesDTO == null) {
            return ResponseEntity.ok(ResponseWrapper.ok("Elective Subject With ID " + electiveSubjectId + " Doesn't Have Serialization"));
        }

        return ResponseEntity.ok(ResponseWrapper.found(subjectSeriesDTO, "Subject Series"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new subject series", description = "Creates a new subject series using the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subject series created", content = @Content(schema = @Schema(implementation = SubjectSeriesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<ResponseWrapper<SubjectSeriesDTO>> createSubjectSeries(@Valid @RequestBody SubjectSeriesInsertDTO subjectSeriesInsertDTO) {

        Result<Void> validationResult = subjectSeriesService.validateSubjectSeriesCreation(subjectSeriesInsertDTO);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(validationResult.getErrorMessage()));
        }

        SubjectSeriesDTO subjectSeriesDTO = subjectSeriesService.createSubjectSeries(subjectSeriesInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.found(subjectSeriesDTO, "Subject Series"));
    }

    @DeleteMapping("/{subjectSeriesId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a subject series by ID", description = "Deletes the subject series with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject series deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Subject series not found", content = @Content)
    })
    public ResponseEntity<ResponseWrapper<SubjectSeriesDTO>> deleteSubjectSeries(@Valid @PathVariable Long subjectSeriesId) {

        subjectSeriesService.deleteSubjectSeriesById(subjectSeriesId);

        return ResponseEntity.ok(ResponseWrapper.deleted("Subject Series"));
    }
}
