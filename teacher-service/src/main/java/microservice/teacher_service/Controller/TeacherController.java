package microservice.teacher_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.teacher_service.DTOs.TeacherDTO;
import microservice.teacher_service.DTOs.TeacherInsertDTO;
import microservice.teacher_service.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/{teacherId}")
    @Operation(summary = "Get teacher by ID", description = "Fetches a teacher by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher data successfully fetched"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    public ResponseEntity<ResponseWrapper<TeacherDTO>> getTeacherById(@PathVariable Long teacherId) {
        Result<TeacherDTO> teacherResult = teacherService.getTeacherById(teacherId);
        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(teacherResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(teacherResult.getData(), "Teacher data successfully fetched"));
    }

    @Operation(summary = "Get teacher by account number", description = "Fetches a teacher by their account number.")
    @GetMapping("/by-accountNumber/{accountNumber}")
    public ResponseEntity<ResponseWrapper<TeacherDTO>> getTeacherById(@PathVariable String accountNumber) {
        Result<TeacherDTO> teacherResult = teacherService.getTeacherByAccountNumber(accountNumber);
        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(teacherResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(teacherResult.getData(), "Teacher data successfully fetched"));
    }

    @Operation(summary = "Get all teachers sorted by account number", description = "Fetches a paginated list of teachers sorted by account number.")
    @GetMapping("/all-by-accountNumber")
    public ResponseEntity<ResponseWrapper<Page<TeacherDTO>>> getAllTeachersSortedByAccountNumber(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                                 @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeacherDTO> teacherDTOS = teacherService.getAllTeachersSortedByAccountNumber(pageable, sortDirection);

        return ResponseEntity.ok(ResponseWrapper.ok(teacherDTOS, "Teacher data successfully fetched"));
    }

    @Operation(summary = "Get all teachers sorted by last name", description = "Fetches a paginated list of teachers sorted by last name.")
    @GetMapping("/all-by-lastname")
    public ResponseEntity<ResponseWrapper<Page<TeacherDTO>>> getAllTeachersSortedByLastname(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeacherDTO> teacherDTOS = teacherService.getAllTeachersSortedByLastname(pageable, sortDirection);

        return ResponseEntity.ok(ResponseWrapper.ok(teacherDTOS, "Teacher data successfully fetched"));
    }


    // Data Validation failures handled by global exception handler
    @Operation(summary = "Create a new teacher", description = "Creates a new teacher entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createTeacher(@Valid @RequestBody TeacherInsertDTO teacherInsertDTO) {
        teacherService.createTeacher(teacherInsertDTO);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Teacher successfully created"));
    }

    @Operation(summary = "Update teacher data", description = "Updates the personal data of an existing teacher.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher successfully updated"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @PutMapping("/{teacherId}")
    public ResponseEntity<ResponseWrapper<Void>> updatePersonalTeacherData(@Valid @RequestBody TeacherInsertDTO teacherInsertDTO,
                                                                           @PathVariable Long teacherId) {
        teacherService.updateTeacher(teacherInsertDTO, teacherId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Teacher successfully updated"));
    }

    @DeleteMapping("/{teacherId}")
    @Operation(summary = "Delete teacher by ID", description = "Deletes a teacher by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    public ResponseEntity<ResponseWrapper<TeacherDTO>> deleteTeacherById(@PathVariable Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Teacher successfully deleted"));
    }


    // endpoint used to validate teacher data integrity in another services
    @Operation(summary = "Validate existing teacher by ID", description = "Validates if a teacher exists by their ID.")
    @GetMapping("/validate/{teacherId}")
    public boolean validateExistingTeacherById(@PathVariable Long teacherId) {
        return teacherService.validateExistingTeacher(teacherId);
    }
}
