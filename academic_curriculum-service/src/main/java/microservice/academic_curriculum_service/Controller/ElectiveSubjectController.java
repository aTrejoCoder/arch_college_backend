package microservice.academic_curriculum_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.academic_curriculum_service.Service.SubjectService;
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

    @Operation(summary = "Get Elective AcademicCurriculumService by ID", description = "Fetches an elective subject by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective AcademicCurriculumService found", content = @Content(schema = @Schema(implementation = ElectiveSubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Elective AcademicCurriculumService not found", content = @Content)
    })
    @GetMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> getElectiveSubjectById(@PathVariable Long subjectId) {
        Result<ElectiveSubjectDTO> areaResult = subjectService.getSubjectById(subjectId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Elective AcademicCurriculumService", "ID", subjectId));
        }
        return ResponseEntity.ok(ResponseWrapper.found(areaResult.getData(),"Elective AcademicCurriculumService", "ID", subjectId));
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
        return ResponseEntity.ok(ResponseWrapper.found(electiveSubjectDTOS,"Elective AcademicCurriculumService", "Area ID", areaId));
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
        return ResponseEntity.ok(ResponseWrapper.found(electiveSubjectDTOS,"Elective AcademicCurriculumService", "Professional Line ID", professionalLineId));
    }

    @Operation(summary = "Get Elective AcademicCurriculumService by Name", description = "Fetches an elective subject by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective AcademicCurriculumService found", content = @Content(schema = @Schema(implementation = ElectiveSubjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Elective AcademicCurriculumService not found", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> getElectiveSubjectByName(@PathVariable String name) {
        Result<ElectiveSubjectDTO> areaResult = subjectService.getSubjectByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("ElectiveSubject", "name", name));
        }
        return ResponseEntity.ok(ResponseWrapper.found(areaResult.getData(),"Elective AcademicCurriculumService", "name", name));
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

        return ResponseEntity.ok(ResponseWrapper.found(ordinarySubjectDTOS,"Elective AcademicCurriculumService", "career Id", careerId));
    }

    @Operation(summary = "Create Elective AcademicCurriculumService", description = "Creates a new elective subject")
    @ApiResponse(responseCode = "201", description = "Elective AcademicCurriculumService created", content = @Content)
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createElectiveSubject(@Valid @RequestBody ElectiveSubjectInsertDTO areaInsertDTO) {
        subjectService.createSubject(areaInsertDTO);
        return ResponseEntity.ok(ResponseWrapper.created(null,"Elective AcademicCurriculumService"));
    }

    @Operation(summary = "Update Elective AcademicCurriculumService", description = "Updates an existing elective subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective AcademicCurriculumService updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Elective AcademicCurriculumService not found", content = @Content)
    })
    @PutMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> updateElectiveSubject(@Valid @RequestBody ElectiveSubjectInsertDTO electiveInsertDTO,
                                                                       @PathVariable Long subjectId) {
        subjectService.updateSubject(electiveInsertDTO, subjectId);
        return ResponseEntity.ok(ResponseWrapper.updated(null,"Elective AcademicCurriculumService"));
    }

    @Operation(summary = "Delete Elective AcademicCurriculumService", description = "Deletes an elective subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elective AcademicCurriculumService deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Elective AcademicCurriculumService not found", content = @Content)
    })
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteElectiveSubjectById(@PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(ResponseWrapper.deleted(null,"Elective AcademicCurriculumService"));
    }
}
