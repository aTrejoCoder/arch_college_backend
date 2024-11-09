package microservice.grade_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.grade_service.DTOs.GradeDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: Security --> Validation

@RestController
@RequestMapping("/v1/api/grades")
public class GradeController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/{gradeId}")
    @Operation(summary = "Get grade by ID", description = "Fetches a grade by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade data successfully fetched"),
            @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    public ResponseEntity<ResponseWrapper<GradeDTO>> getGradeById(@PathVariable Long gradeId) {
        Result<GradeDTO> gradeResult = gradeService.getGradeById(gradeId);
        if (!gradeResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(gradeResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(gradeResult.getData(), "Grade data successfully fetched"));
    }

    // Data Validation failures handled by global exception handler
    @Operation(summary = "Create a new grade cursed in an schooled modality", description = "Init a new grade entry, with validation pending.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<GradeDTO>> initSchooledGrade(@Valid @RequestBody GradeInsertDTO gradeInsertDTO) {
        Result<Void> creation = gradeService.validateGradeCreation(gradeInsertDTO);
        if (!creation.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(creation.getErrorMessage()));
        }

        Result<Void> relationshipResult = gradeService.validateGradeRelationships(gradeInsertDTO);
        if (!relationshipResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(relationshipResult.getErrorMessage()));
        }

        gradeService.initGrade(gradeInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.ok(null, "Grade successfully created"));
    }

    @Operation(summary = "Authorize an exisiting grade and after validation this grades is part of student grades", description = "Validate an exisiting grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade successfully created"),
            @ApiResponse(responseCode = "400", description = "Grade not able to be authorized")
    })
    @PutMapping("{gradeId}/authorize")
    public ResponseEntity<ResponseWrapper<GradeDTO>> authorizeGrade(@PathVariable Long gradeId) {
        Result<Void> gradeResult = gradeService.authorizeGradeById(gradeId);
        if (!gradeResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(gradeResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(null, "Grade successfully approved"));
    }


    @DeleteMapping("/{gradeId}")
    @Operation(summary = "Delete grade by ID", description = "Delete a grade by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    public ResponseEntity<ResponseWrapper<GradeDTO>> deleteGradeById(@PathVariable Long gradeId) {
        gradeService.deleteGradeById(gradeId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Grade successfully deleted"));
    }


    @GetMapping("student/{studentId}/all")
    @Operation(summary = "Get all grades by student ID", description = "Fetches a grades by student ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade data successfully fetched"),
    })
    public ResponseEntity<ResponseWrapper<List<GradeDTO>>> getGradesByStudentId(@PathVariable Long studentId) {
        List<GradeDTO> grades = gradeService.getAllGradeByStudent(studentId);
        return ResponseEntity.ok(ResponseWrapper.found("Student", "Student ID", studentId, grades));
    }


    @GetMapping("student/{studentId}/school-period/{schoolPeriod}")
    @Operation(summary = "Get grades by student ID from certain school period", description = "Fetches a grades by student ID in some semester.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade data successfully fetched"),
    })
    public ResponseEntity<ResponseWrapper<List<GradeDTO>>> getGradesById(@PathVariable Long studentId, String schoolPeriod) {
        List<GradeDTO> grades = gradeService.getGradesByStudentAndSchoolPeriod(studentId, schoolPeriod);
        return ResponseEntity.ok(ResponseWrapper.found("Student", "Student ID", studentId, grades));
    }
}
