package microservice.teacher_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.common_classes.Utils.Teacher.Title;
import microservice.teacher_service.DTOs.TeacherDTO;
import microservice.teacher_service.Service.TeacherFinderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/api/teachers")
@RequiredArgsConstructor
public class TeacherFinderController {

    private final TeacherFinderService teacherFinderService;

    @GetMapping("/{teacherId}")
    @Operation(summary = "Get teacher by ID", description = "Fetches a teacher by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher data successfully fetched"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    public ResponseEntity<ResponseWrapper<TeacherDTO>> getTeacherById(@PathVariable Long teacherId) {
        Result<TeacherDTO> teacherResult = teacherFinderService.getTeacherById(teacherId);
        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(teacherResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(teacherResult.getData(), "Teacher data successfully fetched"));
    }

    @Operation(summary = "Get teacher by account number", description = "Fetches a teacher by their account number.")
    @GetMapping("/by-accountNumber/{accountNumber}")
    public ResponseEntity<ResponseWrapper<TeacherDTO>> getTeacherByAccountNumber(@PathVariable String accountNumber) {
        Result<TeacherDTO> teacherResult = teacherFinderService.getTeacherByAccountNumber(accountNumber);
        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(teacherResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(teacherResult.getData(), "Teacher data successfully fetched"));
    }

    @Operation(summary = "Get teacher by account number", description = "Fetches a teacher by their account number.")
    @GetMapping("/by-ids/{idSet}")
    public ResponseEntity<ResponseWrapper<List<TeacherDTO>>> getTeacherByIds(@PathVariable Set<Long> idSet) {
        Result<List<TeacherDTO>> teachersResult = teacherFinderService.getTeachersByIds(idSet);
        if (!teachersResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(teachersResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(teachersResult.getData(), "Teacher data successfully fetched"));
    }

    @Operation(
            summary = "Get all teachers with sorting",
            description = "Fetches a paginated list of teachers with optional sorting by account number, title, or last name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<Page<TeacherDTO>>> getAllTeachers(
            @Parameter(description = "Page number for pagination", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Page size for pagination", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,

            @Parameter(description = "Sort direction: ASC (ascending) or DESC (descending)", example = "ASC")
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,

            @Parameter(
                    description = "Field to sort by. Allowed values: accountNumber, title, lastName",
                    example = "accountNumber",
                    allowEmptyValue = false,
                    schema = @Schema(allowableValues = {"accountNumber", "title", "lastName"})
            )
            @RequestParam(value = "sortBy", defaultValue = "accountNumber") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeacherDTO> teacherDTOS = teacherFinderService.getAllTeachersSorted(pageable, sortDirection, sortBy);

        return ResponseEntity.ok(ResponseWrapper.ok(teacherDTOS, "Teacher data successfully fetched"));
    }


    @GetMapping("/by-title")
    public ResponseEntity<ResponseWrapper<Page<TeacherDTO>>> getTeachersByTitlePageable(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                                 @PathVariable Title title) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeacherDTO> teacherDTOS = teacherFinderService.getTeachersByTitlePageable(title, pageable);

        return ResponseEntity.ok(ResponseWrapper.ok(teacherDTOS, "Teacher data successfully fetched"));
    }
}
