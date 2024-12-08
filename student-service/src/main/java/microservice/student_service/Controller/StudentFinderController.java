package microservice.student_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Student.StudentFilter;
import microservice.student_service.Service.StudentFinderService;
import microservice.student_service.Utils.StudentFinderValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/api/students")
@RequiredArgsConstructor
public class StudentFinderController {

    private final StudentFinderService studentFinderService;
    private final StudentFinderValidator studentFinderValidator;
    
    @GetMapping("/{studentId}")
    @Operation(summary = "Get student by ID", description = "Fetches a student by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student data successfully fetched"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<ResponseWrapper<StudentDTO>> getStudentById(@PathVariable Long studentId) {
        Result<StudentDTO> studentResult = studentFinderService.getStudentById(studentId);
        if (!studentResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(studentResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(studentResult.getData(), "Student data successfully fetched"));
    }

    @Operation(summary = "Get student by account number", description = "Fetches a student by their account number.")
    @GetMapping("/by-accountNumber/{accountNumber}")
    public ResponseEntity<ResponseWrapper<StudentDTO>> getStudentById(@PathVariable String accountNumber) {
        Result<StudentDTO> studentResult = studentFinderService.getStudentByAccountNumber(accountNumber);
        if (!studentResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(studentResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(studentResult.getData(), "Student data successfully fetched"));
    }


    /**
     * Endpoint to get all students with pagination and sorting based on a specific filter.
     *
     * @param page The page number for pagination.
     * @param size The number of elements per page.
     * @param filter The filter by which the students will be sorted (e.g., "SEMESTERS_COMPLETED").
     * @param sortOrder The sort order, can be "asc" for ascending or "desc" for descending.
     * @return ResponseEntity containing the paginated and filtered list of students.
     */
    @Operation(
            summary = "Get all students sorted by a specific filter",
            description = "This endpoint retrieves a paginated list of students sorted by a specific filter.",
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0", required = false),
                    @Parameter(name = "size", description = "Number of items per page", example = "10", required = false),
                    @Parameter(name = "filter", description = "Filter by which the students will be sorted", example = "SEMESTERS_COMPLETED", required = false),
                    @Parameter(name = "sortOrder", description = "Sort order, can be 'asc' or 'desc'", example = "asc", required = false)
            }
    )
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<Page<StudentDTO>>> getAllStudentsSortedByFilter(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "filter", defaultValue = "SEMESTERS_COMPLETED") String filter,
            @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) {

        Result<StudentFilter> filterValidation = studentFinderValidator.validateStudentFilter(filter);
        if (!filterValidation.isSuccess()) {
            return ResponseEntity.badRequest().body(ResponseWrapper.badRequest(filterValidation.getErrorMessage()));
        }
        StudentFilter studentFilter = filterValidation.getData();


        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, studentFilter.getEntityField()));

        Page<StudentDTO> studentDTOS = studentFinderService.getAllStudentsSortedByFilterPageable(pageable,studentFilter);

        return ResponseEntity.ok(ResponseWrapper.ok(studentDTOS, "Student data successfully fetched with filter: " + filter + " ("+  sortOrder +")"));
    }


    /**
     * Endpoint to get students filtered by a specific value with pagination.
     *
     * @param page The page number for pagination.
     * @param size The number of elements per page.
     * @param filter The filter to apply (e.g., "SEMESTERS_COMPLETED").
     * @param param The value to use with the filter for the search.
     * @param sortOrder The sort order, can be "asc" for ascending or "desc" for descending.
     * @return ResponseEntity containing the paginated and filtered list of students.
     */
    @Operation(
            summary = "Get students filtered by a specific value with pagination",
            description = "This endpoint retrieves a paginated list of students filtered by a specific value.",
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0", required = false),
                    @Parameter(name = "size", description = "Number of items per page", example = "10", required = false),
                    @Parameter(name = "filter", description = "Filter to apply", example = "SEMESTERS_COMPLETED", required = true),
                    @Parameter(name = "param", description = "Value to use with the filter for the search", example = "5", required = true),
                    @Parameter(name = "sortOrder", description = "Sort order, can be 'asc' or 'desc'", example = "asc", required = false)
            }
    )
    @GetMapping("/by")
    public ResponseEntity<ResponseWrapper<Page<StudentDTO>>> getStudentsByFilterPageable(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "filter", defaultValue = "SEMESTERS_COMPLETED") String filter,
            @RequestParam(value = "param", defaultValue = "0") String param,
            @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) {
        boolean isValid = StudentFilter.isValid(filter);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.badRequest("Invalid filter. Valid filters: " + Arrays.toString(StudentFilter.getFinderFilters())));
        }
        Result<StudentFilter> filterValidation = studentFinderValidator.validateStudentFilter(filter);
        if (!filterValidation.isSuccess()) {
            return ResponseEntity.badRequest().body(ResponseWrapper.badRequest(filterValidation.getErrorMessage()));
        }
        StudentFilter studentFilter = filterValidation.getData();

        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, studentFilter.getEntityField()));

        Page<StudentDTO> studentDTOS = studentFinderService.getStudentsSortedByFilterPageable(pageable, studentFilter, param);

        return ResponseEntity.ok(ResponseWrapper.ok(studentDTOS, "Student data successfully fetched with filter: " + filter + " = " + param + " ("+  sortOrder +")"));
    }
}
