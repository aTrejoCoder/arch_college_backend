package microservice.enrollment_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.JWT.JWTSecurity;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationship;
import microservice.enrollment_service.Model.Preload.Group;
import microservice.enrollment_service.Service.EnrollmentCommandService;
import microservice.enrollment_service.Service.EnrollmentRelationshipService;
import microservice.enrollment_service.Service.EnrollmentFinderService;
import microservice.enrollment_service.Service.EnrollmentValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/group-enrollments/students")
@RequiredArgsConstructor
@Tag(name = "Student Enrollment API", description = "Endpoints for managing student group enrollments")
public class StudentEnrollmentController {

    private final EnrollmentFinderService enrollmentFinderService;
    private final EnrollmentCommandService enrollmentCommandService;
    private final EnrollmentValidationService enrollmentValidationService;
    private final EnrollmentRelationshipService enrollmentRelationshipService;
    private final JWTSecurity jwtSecurity;

    @GetMapping("/my-enrollments")
    @Operation(summary = "Get my enrollments", description = "Fetches all enrollments associated with the logged-in student's account number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<ResponseWrapper<List<EnrollmentDTO>>> getMyEnrollments(HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        List<EnrollmentDTO> enrollments = enrollmentFinderService.getByAccountNumber(accountNumber);

        return ResponseEntity.ok(ResponseWrapper.found(enrollments, "Enrollments"));
    }

    @PostMapping
    @Operation(summary = "Make an enrollment", description = "Creates a new group enrollment for the logged-in student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Enrollment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Enrollment validation failed or group does not exist")
    })
    public ResponseEntity<ResponseWrapper<Void>> makeAnEnrollment(
            @Valid @RequestBody EnrollmentInsertDTO enrollmentInsertDTO,
            HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        Result<Group> groupResult = enrollmentRelationshipService.validateExistingGroup(enrollmentInsertDTO);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.conflict(groupResult.getErrorMessage()));
        }

        EnrollmentRelationship enrollmentRelationship = enrollmentRelationshipService.getRelationshipData(groupResult.getData(), accountNumber);

        Result<Void> validationResult = enrollmentValidationService.validateEnrollment(enrollmentInsertDTO, enrollmentRelationship, accountNumber);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(validationResult.getErrorMessage()));
        }

        enrollmentCommandService.createEnrollment(enrollmentRelationship, enrollmentInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created("Group Enrollment successfully created"));
    }

    @DeleteMapping("/group/{groupKey}/subject/{subjectKey}")
    @Operation(summary = "Delete an enrollment", description = "Deletes an existing group enrollment for the logged-in student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<ResponseWrapper<Void>> deleteAnEnrollment(
            @Valid @PathVariable String groupKey,
            @PathVariable String subjectKey,
            HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        Result<Void> deleteResult = enrollmentCommandService.deleteEnrollment(groupKey, subjectKey, accountNumber);
        if (!deleteResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.badRequest(deleteResult.getErrorMessage()));
        }

        return ResponseEntity.ok().body(ResponseWrapper.deleted("Enrollment"));
    }
}
