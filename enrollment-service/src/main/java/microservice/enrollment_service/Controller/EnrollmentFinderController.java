package microservice.enrollment_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.Service.EnrollmentFinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/group-enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollment Finder API", description = "Endpoints for fetching enrollment data")
public class EnrollmentFinderController {

    private final EnrollmentFinderService enrollmentFinderService;

    @GetMapping("/{enrollmentId}")
    @Operation(summary = "Get enrollment by ID", description = "Fetches an enrollment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GroupEnrollment data successfully fetched"),
            @ApiResponse(responseCode = "404", description = "GroupEnrollment not found")
    })
    public ResponseEntity<ResponseWrapper<EnrollmentDTO>> getEnrollmentById(@PathVariable Long enrollmentId) {
        Result<EnrollmentDTO> enrollmentResult = enrollmentFinderService.getById(enrollmentId);
        if (!enrollmentResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(enrollmentResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(enrollmentResult.getData(), "GroupEnrollment data successfully fetched"));
    }

    @GetMapping("/by-student/{studentAccountNumber}")
    @Operation(summary = "Get enrollments by student account number", description = "Fetches all enrollments for a given student account number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments successfully fetched"),
            @ApiResponse(responseCode = "404", description = "No enrollments found for the given student")
    })
    public ResponseEntity<ResponseWrapper<List<EnrollmentDTO>>> getEnrollmentsByAccountNumber(@Valid @PathVariable String studentAccountNumber) {
        List<EnrollmentDTO> enrollments = enrollmentFinderService.getByAccountNumber(studentAccountNumber);
        return ResponseEntity.ok(ResponseWrapper.ok(enrollments, "GroupEnrollment data successfully fetched"));
    }
}
