package microservice.enrollment_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;
import microservice.enrollment_service.Service.EnrollmentRelationshipService;
import microservice.enrollment_service.Service.EnrollmentService;
import microservice.enrollment_service.Service.EnrollmentValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Security --> Validation

@RestController
@RequestMapping("/v1/api/group-enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final EnrollmentRelationshipService enrollmentRelationshipService;
    private final EnrollmentValidationService enrollmentValidationService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService,
                                EnrollmentRelationshipService enrollmentRelationshipService,
                                EnrollmentValidationService enrollmentValidationService) {
        this.enrollmentService = enrollmentService;
        this.enrollmentRelationshipService = enrollmentRelationshipService;
        this.enrollmentValidationService = enrollmentValidationService;
    }

    @GetMapping("/{enrollmentId}")
    @Operation(summary = "Get enrollment by ID", description = "Fetches a enrollment by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GroupEnrollment data successfully fetched"),
            @ApiResponse(responseCode = "404", description = "GroupEnrollment not found")
    })
    public ResponseEntity<ResponseWrapper<EnrollmentDTO>> getEnrollmentById(@PathVariable Long enrollmentId) {
        Result<EnrollmentDTO> enrollmentResult = enrollmentService.getEnrollmentById(enrollmentId);
        if (!enrollmentResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(enrollmentResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(enrollmentResult.getData(), "GroupEnrollment data successfully fetched"));
    }

    // Data Validation failures handled by global exception handler
    @Operation(summary = "Create a new subject enrollment", description = "Creates a new enrollment entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GroupEnrollment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/{studentAccountNumber}")
    public ResponseEntity<ResponseWrapper<EnrollmentDTO>> createSubjectEnrollment(@Valid @RequestBody EnrollmentInsertDTO enrollmentInsertDTO,
                                                                                  @PathVariable String studentAccountNumber) {
        Result<EnrollmentRelationshipDTO> relationshipsResult = enrollmentRelationshipService.validateAndGetRelationships(enrollmentInsertDTO, studentAccountNumber);
        if (!relationshipsResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(relationshipsResult.getErrorMessage()));
        }

        Result<Void> validationResult = enrollmentValidationService.validateEnrollment(enrollmentInsertDTO, relationshipsResult.getData(), studentAccountNumber);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(validationResult.getErrorMessage()));
        }

        Result<Void> spotResult = enrollmentRelationshipService.takeSpot(enrollmentInsertDTO.getGroupKey());
        if (!spotResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(spotResult.getErrorMessage()));
        }

        enrollmentService.createEnrollment(relationshipsResult.getData(), enrollmentInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created("Group Enrollment successfully created"));
    }

    @Operation(summary = "Create a new subject enrollment", description = "Creates a new enrollment entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GroupEnrollment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @GetMapping("/by-student/{studentAccountNumber}")
    public ResponseEntity<Object> get(@Valid @PathVariable String studentAccountNumber) {

        var list = enrollmentService.getEnrollmentsByAccountNumber(studentAccountNumber);

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{enrollmentId}")
    @Operation(summary = "Delete subject enrollment by ID", description = "Deletes a enrollment by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GroupEnrollment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "GroupEnrollment not found")
    })
    public ResponseEntity<ResponseWrapper<Void>> deleteEnrollmentById(@PathVariable Long enrollmentId) {

        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "GroupEnrollment successfully deleted"));
    }
}
