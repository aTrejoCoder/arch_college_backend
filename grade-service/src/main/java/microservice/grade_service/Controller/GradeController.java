package microservice.grade_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.DTOs.GradeRelationshipsDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Service.GradeRelationshipService;
import microservice.grade_service.Service.GradeService;
import microservice.grade_service.Service.GradeValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: Security --> Validation

@RestController
@RequestMapping("/v1/api/grades")
@RequiredArgsConstructor
public class GradeController {
    private final GradeRelationshipService gradeRelationshipService;
    private final GradeService gradeService;
    private final GradeValidationService gradeValidationService;


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
        Result<GradeRelationshipsDTO> relationshipResult = gradeRelationshipService.validateGradeRelationship(gradeInsertDTO);
        if (!relationshipResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(relationshipResult.getErrorMessage()));
        }

        Result<Void> creationResult = gradeValidationService.validateGradeCreation(gradeInsertDTO);
        if (!creationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(creationResult.getErrorMessage()));
        }

        gradeService.initGrade(gradeInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.ok("Grade successfully created"));
    }

    @Operation(summary = "Authorize an exisiting grade and after validation this grades is part of student grades", description = "Validate an exisiting grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade successfully created"),
            @ApiResponse(responseCode = "400", description = "Grade not able to be authorized")
    })
    @PutMapping("{gradeId}/authorize")
    public ResponseEntity<ResponseWrapper<GradeDTO>> authorizeGrade(@PathVariable Long gradeId) {
        Result<Void> gradeResult = gradeValidationService.authorizeGradeById(gradeId);
        if (!gradeResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(gradeResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok("Grade successfully approved"));
    }


    @DeleteMapping("/{gradeId}")
    @Operation(summary = "Delete grade by ID", description = "Delete a grade by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    public ResponseEntity<ResponseWrapper<GradeDTO>> softDeleteGradeById(@PathVariable Long gradeId) {
        gradeService.deleteGradeById(gradeId);
        return ResponseEntity.ok(ResponseWrapper.ok("Grade successfully deleted"));
    }


    @GetMapping("student/{accountNumber}/all")
    @Operation(summary = "Get all grades by student ID", description = "Fetches a grades by student ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade data successfully fetched"),
    })
    public ResponseEntity<ResponseWrapper<List<GradeDTO>>> getGradesByStudentAccountNumber(@PathVariable String accountNumber) {
        List<GradeDTO> grades = gradeService.getAllGradeByStudentAccountNumber(accountNumber);
        return ResponseEntity.ok(ResponseWrapper.found(grades, "Student", "Student Account Number", accountNumber));
    }


    @GetMapping("student/{studentId}/school-period/{schoolPeriod}")
    @Operation(summary = "Get grades by student ID from certain school period", description = "Fetches a grades by student ID in some semester.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade data successfully fetched"),
    })
    public ResponseEntity<ResponseWrapper<List<GradeDTO>>> getGradesById(@PathVariable String accountNumber, String schoolPeriod) {
        List<GradeDTO> grades = gradeService.getGradesByStudentAccountNumber(accountNumber, schoolPeriod);
        return ResponseEntity.ok(ResponseWrapper.found(grades,"Student", "Student Account Number", accountNumber));
    }
}
