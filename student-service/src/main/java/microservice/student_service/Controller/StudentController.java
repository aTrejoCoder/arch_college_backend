package microservice.student_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Student.StudentInsertDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.student_service.Service.StudentRelationService;
import microservice.student_service.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentRelationService studentRelationService;

    @Autowired
    public StudentController(StudentService studentService,
                             StudentRelationService studentRelationService) {
        this.studentService = studentService;
        this.studentRelationService = studentRelationService;
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "Get student by ID", description = "Fetches a student by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student data successfully fetched"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<ResponseWrapper<StudentDTO>> getStudentById(@PathVariable Long studentId) {
        Result<StudentDTO> studentResult = studentService.getStudentById(studentId);
        if (!studentResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(studentResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(studentResult.getData(), "Student data successfully fetched"));
    }

    @Operation(summary = "Get student by account number", description = "Fetches a student by their account number.")
    @GetMapping("/by-accountNumber/{accountNumber}")
    public ResponseEntity<ResponseWrapper<StudentDTO>> getStudentById(@PathVariable String accountNumber) {
        Result<StudentDTO> studentResult = studentService.getStudentByAccountNumber(accountNumber);
        if (!studentResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(studentResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(studentResult.getData(), "Student data successfully fetched"));
    }

    @Operation(summary = "Get all students sorted by account number", description = "Fetches a paginated list of students sorted by account number.")
    @GetMapping("/all-by-accountNumber")
    public ResponseEntity<ResponseWrapper<Page<StudentDTO>>> getAllStudentsSortedByAccountNumber(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                                 @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentDTO> studentDTOS = studentService.getAllStudentsSortedByAccountNumber(pageable, sortDirection);

        return ResponseEntity.ok(ResponseWrapper.ok(studentDTOS, "Student data successfully fetched"));
    }

    @Operation(summary = "Get all students sorted by last name", description = "Fetches a paginated list of students sorted by last name.")
    @GetMapping("/all-by-lastname")
    public ResponseEntity<ResponseWrapper<Page<StudentDTO>>> getAllStudentsSortedByLastname(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentDTO> studentDTOS = studentService.getAllStudentsSortedByLastname(pageable, sortDirection);

        return ResponseEntity.ok(ResponseWrapper.ok(studentDTOS, "Student data successfully fetched"));
    }


    // Data Validation failures handled by global exception handler
    @Operation(summary = "Create a new student", description = "Creates a new student entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createStudent(@Valid @RequestBody StudentInsertDTO studentInsertDTO) {
        Result<Void> careerResult = studentRelationService.validateExistingCareerId(studentInsertDTO.getCareerId());
        if (!careerResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(careerResult.getErrorMessage()));
        }

        studentService.createStudent(studentInsertDTO);

        // Create Academic History

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created("Student successfully created"));
    }


    @Operation(summary = "Update student data", description = "Updates the personal data of an existing student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student successfully updated"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper<Void>> updatePersonalStudentData(@Valid @RequestBody StudentInsertDTO studentInsertDTO,
                                                                           @PathVariable Long studentId) {
        studentService.updateStudent(studentInsertDTO, studentId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Student successfully updated"));
    }

    @DeleteMapping("/{studentId}")
    @Operation(summary = "Delete student by ID", description = "Deletes a student by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<ResponseWrapper<StudentDTO>> deleteStudentById(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Student successfully deleted"));
    }


    // endpoint used to validate student data integrity in another services
    @Operation(summary = "Validate existing student by ID", description = "Validates if a student exists by their ID.")
    @GetMapping("/validate/{studentId}")
    public boolean validateExistingStudentById(@PathVariable Long studentId) {
        return studentService.validateExistingStudent(studentId);
    }

    @PatchMapping("/{studentAccount}/set-professionalLine/{professionalLineId}/modality/{professionalLineModality}")
    public ResponseEntity<Void> setProfessionalLineData(@Valid @PathVariable String studentAccount,
                                                        @PathVariable ProfessionalLineModality professionalLineModality,
                                                        @PathVariable Long professionalLineId) {
        studentService.setProfessionalLineData(studentAccount, professionalLineId, professionalLineModality);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{studentAccount}/increase-semester-completed")
    public ResponseEntity<Void> increaseSemesterCompleted(@Valid @PathVariable String studentAccount) {
        studentService.increaseSemestersCursed(studentAccount);
        return ResponseEntity.ok().build();
    }
}
