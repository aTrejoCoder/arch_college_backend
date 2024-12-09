package microservice.grade_service.Controller.Grade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.SubjectType;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.grade_service.Service.GradeFinderService;
import microservice.grade_service.Utils.Credits.GradeFinderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/grades")
@RequiredArgsConstructor
public class GradeFinderController {
    private final GradeFinderService gradeFinderService;

    @GetMapping("/{gradeId}")
    @Operation(
            summary = "Get grade by ID",
            description = "Fetches a grade by its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Grade data successfully fetched",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Grade not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<ResponseWrapper<GradeDTO>> getGradeById(@PathVariable Long gradeId) {
        Result<GradeDTO> gradeResult = gradeFinderService.getGradeById(gradeId);
        if (!gradeResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(gradeResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.ok(gradeResult.getData(), "Grade data successfully fetched"));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Find grades by filters",
            description = "Retrieves a list of grades based on various optional filters such as account number, school period, subject ID, and subject type. Supports pagination and sorting."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Grades successfully fetched",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<ResponseWrapper<Page<GradeDTO>>> findGrades(
            @Parameter(description = "Optional filter by student account number") @RequestParam(required = false) String accountNumber,
            @Parameter(description = "Optional filter by school period (e.g., '2024-2 OR 2025-1')") @RequestParam(required = false) String schoolPeriod,
            @Parameter(description = "Optional filter by subject ID") @RequestParam(required = false) Long subjectId,
            @Parameter(description = "Optional filter by subject type (e.g., 'OBLIGATORY' or 'ELECTIVE')") @RequestParam(required = false) SubjectType subjectType,
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of results per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort order in the format 'property,direction'. Example: 'id,asc'") @RequestParam(defaultValue = "id,asc") String sort
    ) {
        GradeFinderFilter gradeFilter = new GradeFinderFilter(accountNumber, schoolPeriod, subjectId, subjectType);

        Pageable pageable = PageRequest.of(
                page,
                size,
                parseSortString(sort)
        );

        Page<GradeDTO> grades = gradeFinderService.getGradesByFilters(gradeFilter, pageable);

        return ResponseEntity.ok(ResponseWrapper.ok(grades, "Student Successfully Fetched By: " + gradeFilter.getActiveFilters()));
    }

    @GetMapping("/pending-validation")
    public ResponseEntity<ResponseWrapper<Page<GradeDTO>>> getPendingValidationGrades(@Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
                                                                            @Parameter(description = "Number of results per page") @RequestParam(defaultValue = "10") int size,
                                                                            @Parameter(description = "Sort order in the format 'direction'. Example: 'id,asc'") @RequestParam(defaultValue = "id,asc") String sort) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                parseSortString(sort)
        );

        Page<GradeDTO> gradePage = gradeFinderService.getPendingValidationGrades(pageable);

        return ResponseEntity.ok(ResponseWrapper.found(gradePage, "Grades with pending validation"));
    }

    private Sort parseSortString(String sort) {
        List<String> allowedProperties = List.of("id", "studentAccountNumber", "schoolPeriod", "subjectId", "subjectType");

        String[] sortParams = sort.split(",");
        if (sortParams.length == 2) {
            String property = sortParams[0].trim();
            String direction = sortParams[1].trim();

            if (allowedProperties.contains(property)) {
                return direction.equalsIgnoreCase("desc")
                        ? Sort.by(Sort.Order.desc(property))
                        : Sort.by(Sort.Order.asc(property));
            }
        }

        return Sort.by(Sort.Order.asc("id"));
    }
}
