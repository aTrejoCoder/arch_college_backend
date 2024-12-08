package microservice.student_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Student.StudentInsertDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.student_service.Service.StudentRelationService;
import microservice.student_service.Service.StudentCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/students")
@RequiredArgsConstructor
public class StudentCommandController {

    private final StudentCommandService studentCommandService;
    private final StudentRelationService studentRelationService;

    // Data Validation failures handled by global exception handler
    @Operation(summary = "Create a new student", description = "Creates a new student entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createStudent(@Valid @RequestBody StudentInsertDTO studentInsertDTO) {
        Result<Void> careerResult = studentRelationService.validateExistingCareerId(studentInsertDTO.getCareerId());
        if (!careerResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(careerResult.getErrorMessage()));
        }

        StudentDTO studentDTO = studentCommandService.createStudent(studentInsertDTO);

        studentRelationService.initAcademicHistoryAsync(studentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created("Student successfully created"));
    }


    @Operation(summary = "Update student data", description = "Updates the personal data of an existing student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student successfully updated"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Void>> updatePersonalStudentData(@Valid @RequestBody StudentInsertDTO studentInsertDTO,
                                                                           @PathVariable Long studentId) {
        studentCommandService.updateStudent(studentInsertDTO, studentId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Student successfully updated"));
    }

    @PostMapping("/{studentAccount}/set-professionalLine/{professionalLineId}/modality/{professionalLineModality}")
    public ResponseEntity<Void> setProfessionalLineData(@Valid @PathVariable String studentAccount,
                                                        @PathVariable ProfessionalLineModality professionalLineModality,
                                                        @PathVariable Long professionalLineId) {
        studentCommandService.setProfessionalLineData(studentAccount, professionalLineId, professionalLineModality);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{studentAccount}/increase-semester-completed")
    public ResponseEntity<Void> increaseSemesterCompleted(@Valid @PathVariable String studentAccount) {
        studentCommandService.increaseSemestersCursed(studentAccount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete student by ID", description = "Deletes a student by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper<StudentDTO>> deleteStudentById(@PathVariable Long studentId) {
        studentCommandService.deleteStudent(studentId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Student successfully deleted"));
    }
}
