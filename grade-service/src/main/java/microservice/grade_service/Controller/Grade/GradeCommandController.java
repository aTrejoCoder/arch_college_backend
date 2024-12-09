package microservice.grade_service.Controller.Grade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.Service.GradeCommandService;
import microservice.grade_service.Service.GradeValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/grades")
@RequiredArgsConstructor
public class GradeCommandController {

    private final GradeCommandService gradeCommandService;
    private final GradeValidationService gradeValidationService;

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
        gradeCommandService.deleteGradeById(gradeId);
        return ResponseEntity.ok(ResponseWrapper.ok("Grade successfully deleted"));
    }
}
