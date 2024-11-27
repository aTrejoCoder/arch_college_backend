package microservice.student_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.student_service.Service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/students")
@RequiredArgsConstructor
public class StudentFinderController {

    private final StudentService studentService;
    
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
}
