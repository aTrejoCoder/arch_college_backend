package microservice.enrollment_service.Controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.JWT.JWTSecurity;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;
import microservice.enrollment_service.Service.EnrollmentRelationshipService;
import microservice.enrollment_service.Service.EnrollmentService;
import microservice.enrollment_service.Service.EnrollmentValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/group-enrollments/students")
@RequiredArgsConstructor
public class StudentEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final EnrollmentValidationService enrollmentValidationService;
    private final EnrollmentRelationshipService enrollmentRelationshipService;
    private final JWTSecurity jwtSecurity;

    @GetMapping("/my-enrollments")
    public ResponseEntity<ResponseWrapper<List<EnrollmentDTO>>> getMyEnrollments(HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByAccountNumber(accountNumber);

        return ResponseEntity.ok(ResponseWrapper.found(enrollments, "Enrollments"));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> makeAnEnrollment(@Valid @RequestBody EnrollmentInsertDTO enrollmentInsertDTO,
                                                                  HttpServletRequest request)  {
         String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

         Result<EnrollmentRelationshipDTO> relationshipResult = enrollmentRelationshipService.validateAndGetRelationships(enrollmentInsertDTO, accountNumber);
         if (!relationshipResult.isSuccess()) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(relationshipResult.getErrorMessage()));
         }

         Result<Void> validationResult = enrollmentValidationService.validateEnrollment(enrollmentInsertDTO, relationshipResult.getData(), accountNumber);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(validationResult.getErrorMessage()));
        }

        enrollmentService.createEnrollment(relationshipResult.getData(), enrollmentInsertDTO);

        return ResponseEntity.ok().body(ResponseWrapper.created("Enrollment successfully created"));
    }

    @DeleteMapping("/group/{groupKey}/subject/{subjectKey}")
    public ResponseEntity<ResponseWrapper<Void>> deleteAnEnrollment(@Valid @PathVariable String groupKey,
                                                                    @PathVariable String subjectKey,
                                                                    HttpServletRequest request)  {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        Result<Void> deleteResult = enrollmentService.deleteEnrollment(groupKey, subjectKey, accountNumber);
        if (!deleteResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.badRequest(deleteResult.getErrorMessage()));
        }

        return ResponseEntity.ok().body(ResponseWrapper.deleted("Enrollment"));
    }

}
