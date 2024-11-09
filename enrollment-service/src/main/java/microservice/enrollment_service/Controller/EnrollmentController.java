package microservice.enrollment_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.enrollment_service.DTOs.EnrollmentDTO;
import microservice.enrollment_service.DTOs.EnrollmentInsertDTO;
import microservice.enrollment_service.Service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Security --> Validation

@RestController
@RequestMapping("/v1/api/group-enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
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
    @PostMapping
    public ResponseEntity<ResponseWrapper<EnrollmentDTO>> createSubjectEnrollment(@Valid @RequestBody EnrollmentInsertDTO enrollmentInsertDTO) {
        EnrollmentDTO enrollmentDTO = enrollmentService.createEnrollment(enrollmentInsertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.ok(enrollmentDTO, "GroupEnrollment successfully created"));
    }

    @DeleteMapping("/{enrollmentId}")
    @Operation(summary = "Delete subject enrollment by ID", description = "Deletes a enrollment by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GroupEnrollment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "GroupEnrollment not found")
    })
    public ResponseEntity<ResponseWrapper<EnrollmentDTO>> deleteEnrollmentById(@PathVariable Long enrollmentId) {

        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "GroupEnrollment successfully deleted"));
    }
}
