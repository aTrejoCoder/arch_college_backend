package microservice.enrollment_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.enrollment_service.DTOs.EnrollmentRelationship;
import microservice.enrollment_service.Model.Preload.Group;
import microservice.enrollment_service.Service.EnrollmentCommandService;
import microservice.enrollment_service.Service.EnrollmentRelationshipService;
import microservice.enrollment_service.Service.EnrollmentValidationService;
import microservice.enrollment_service.Service.Implementation.EnrollmentLockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/v1/api/group-enrollments")
@RequiredArgsConstructor
public class EnrollmentCommandController {
    private final EnrollmentCommandService enrollmentCommandService;
    private final EnrollmentLockService lockService;
    private final EnrollmentRelationshipService enrollmentRelationshipService;
    private final EnrollmentValidationService enrollmentValidationService;

    @Operation(summary = "Create a new subject enrollment", description = "Creates a new enrollment entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GroupEnrollment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/{studentAccountNumber}")
    public ResponseEntity<ResponseWrapper<EnrollmentDTO>> createSubjectEnrollment(@Valid @RequestBody EnrollmentInsertDTO enrollmentInsertDTO,
                                                                                  @PathVariable String studentAccountNumber) {
        Result<Group> groupResult = enrollmentRelationshipService.validateExistingGroup(enrollmentInsertDTO);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.conflict(groupResult.getErrorMessage()));
        }

        EnrollmentRelationship enrollmentRelationship = enrollmentRelationshipService.getRelationshipData(groupResult.getData(), studentAccountNumber);


        Result<Void> validationResult = enrollmentValidationService.validateEnrollment(enrollmentInsertDTO, enrollmentRelationship, studentAccountNumber);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(validationResult.getErrorMessage()));
        }

        Result<Void> spotResult = enrollmentRelationshipService.takeSpot(enrollmentRelationship.getGroup().getId());
        if (!spotResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(spotResult.getErrorMessage()));
        }

        enrollmentCommandService.createEnrollment(enrollmentRelationship, enrollmentInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created("Group Enrollment successfully created"));
    }

    @DeleteMapping("/{enrollmentId}")
    @Operation(summary = "Delete subject enrollment by ID", description = "Deletes a enrollment by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GroupEnrollment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "GroupEnrollment not found")
    })
    public ResponseEntity<ResponseWrapper<Void>> deleteEnrollmentById(@PathVariable Long enrollmentId) {
        enrollmentCommandService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok(ResponseWrapper.deleted("Enrollment"));
    }

    @GetMapping("/lock-date")
    @Operation(summary = "Get the enrollment lock date", description = "Retrieves the lock date for enrollment and the current school period.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lock date successfully retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseWrapper<LocalDateTime>> getEnrollmentLockDate() {
        LocalDateTime lockDate = lockService.getLockDate();
        String currentSchoolPeriod = AcademicData.getCurrentSchoolPeriod();

        return ResponseEntity.ok(ResponseWrapper.ok(
                lockDate,
                String.format("Semester: %s Enrollment lock date: %s", currentSchoolPeriod, lockDate)
        ));
    }

}
