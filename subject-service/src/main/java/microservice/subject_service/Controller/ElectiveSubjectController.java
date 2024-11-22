package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
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

import java.util.List;

@RestController
@RequestMapping("/v1/api/subjects/electives")
public class ElectiveSubjectController {

    private final SubjectService<ElectiveSubjectDTO ,ElectiveSubjectInsertDTO> subjectService;

    @Autowired
    public ElectiveSubjectController(SubjectService<ElectiveSubjectDTO ,ElectiveSubjectInsertDTO> subjectService) {
        this.subjectService = subjectService;
    }

    @Operation(summary = "Get Elective Subject by ID", description = "Fetches an elective subject by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective Subject found", content = @Content(schema = @Schema(implementation = ElectiveSubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Elective Subject not found", content = @Content)
    })
    @GetMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> getElectiveSubjectById(@PathVariable Long subjectId) {
        Result<ElectiveSubjectDTO> areaResult = subjectService.getSubjectById(subjectId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Elective Subject", "ID", subjectId));
        }
        return ResponseEntity.ok(ResponseWrapper.found(areaResult.getData(),"Elective Subject", "ID", subjectId));
    }

    @Operation(summary = "Get Elective Subjects by Area ID", description = "Fetches elective subjects by area ID")
    @ApiResponse(responseCode = "200", description = "Elective Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("by-area/{areaId}")
    public ResponseEntity<ResponseWrapper<Page<ElectiveSubjectDTO>>> getElectiveSubjectByAreaId(
            @PathVariable Long areaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ElectiveSubjectDTO> electiveSubjectDTOS = subjectService.getSubjectsByFilterPageable(areaId, "area", pageable);
        return ResponseEntity.ok(ResponseWrapper.found(electiveSubjectDTOS,"Elective Subject", "Area ID", areaId));
    }

    @Operation(summary = "Get Elective Subjects by Professional Line ID", description = "Fetches elective subjects by professional line ID")
    @ApiResponse(responseCode = "200", description = "Elective Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("by-professional_line/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<Page<ElectiveSubjectDTO>>> getElectiveSubjectByProfessionalLineId(
            @PathVariable Long professionalLineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ElectiveSubjectDTO> electiveSubjectDTOS = subjectService.getSubjectsByFilterPageable(professionalLineId, "professional line", pageable);
        return ResponseEntity.ok(ResponseWrapper.found(electiveSubjectDTOS,"Elective Subject", "Professional Line ID", professionalLineId));
    }

    @Operation(summary = "Get Elective Subject by Name", description = "Fetches an elective subject by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective Subject found", content = @Content(schema = @Schema(implementation = ElectiveSubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Elective Subject not found", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> getElectiveSubjectByName(@PathVariable String name) {
        Result<ElectiveSubjectDTO> areaResult = subjectService.getSubjectByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("ElectiveSubject", "name", name));
        }
        return ResponseEntity.ok(ResponseWrapper.found(areaResult.getData(),"Elective Subject", "name", name));
    }

    @Operation(summary = "Get All Elective Subjects", description = "Fetches all elective subjects with pagination")
    @ApiResponse(responseCode = "200", description = "Elective Subjects found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<Page<ElectiveSubjectDTO>>> getAllElectiveSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ElectiveSubjectDTO> professionalLines = subjectService.getAllSubjectsPageable(pageable);
        return ResponseEntity.ok(ResponseWrapper.found(professionalLines,"Elective Subjects"));
    }

    @GetMapping("/by-career/{careerId}")
    public ResponseEntity<ResponseWrapper<List<ElectiveSubjectDTO>>> getAllElectiveSubjectByCareer(@PathVariable Long careerId) {
        List<ElectiveSubjectDTO> ordinarySubjectDTOS = subjectService.getSubjectsByFilter(careerId, "career");

        return ResponseEntity.ok(ResponseWrapper.found(ordinarySubjectDTOS,"Elective Subject", "career Id", careerId));
    }

    @Operation(summary = "Create Elective Subject", description = "Creates a new elective subject")
    @ApiResponse(responseCode = "201", description = "Elective Subject created", content = @Content)
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createElectiveSubject(@Valid @RequestBody ElectiveSubjectInsertDTO areaInsertDTO) {
        subjectService.createSubject(areaInsertDTO);
        return ResponseEntity.ok(ResponseWrapper.created(null,"Elective Subject"));
    }

    @Operation(summary = "Update Elective Subject", description = "Updates an existing elective subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective Subject updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Elective Subject not found", content = @Content)
    })
    @PutMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> updateElectiveSubject(@Valid @RequestBody ElectiveSubjectInsertDTO electiveInsertDTO,
                                                                       @PathVariable Long subjectId) {
        subjectService.updateSubject(electiveInsertDTO, subjectId);
        return ResponseEntity.ok(ResponseWrapper.updated(null,"Elective Subject"));
    }

    @Operation(summary = "Delete Elective Subject", description = "Deletes an elective subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective Subject deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Elective Subject not found", content = @Content)
    })
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteElectiveSubjectById(@PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(ResponseWrapper.deleted(null,"Elective Subject"));
    }
}
