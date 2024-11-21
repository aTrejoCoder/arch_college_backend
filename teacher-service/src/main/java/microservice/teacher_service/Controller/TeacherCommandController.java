package microservice.teacher_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.teacher_service.DTOs.TeacherDTO;
import microservice.teacher_service.DTOs.TeacherInsertDTO;
import microservice.teacher_service.Service.TeacherCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/teachers")
@RequiredArgsConstructor
public class TeacherCommandController {

    private final TeacherCommandService teacherCommandService;

    // Data Validation failures handled by global exception handler
    @Operation(summary = "Create a new teacher", description = "Creates a new teacher entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createTeacher(@Valid @RequestBody TeacherInsertDTO teacherInsertDTO) {
        teacherCommandService.createTeacher(teacherInsertDTO);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Teacher successfully created"));
    }

    @Operation(summary = "Update teacher data", description = "Updates the personal data of an existing teacher.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher successfully updated"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{teacherId}")
    public ResponseEntity<ResponseWrapper<Void>> updatePersonalTeacherData(@Valid @RequestBody TeacherInsertDTO teacherInsertDTO,
                                                                           @PathVariable Long teacherId) {
        teacherCommandService.updateTeacher(teacherInsertDTO, teacherId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Teacher successfully updated"));
    }

    @Operation(summary = "Delete teacher by ID", description = "Deletes a teacher by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<ResponseWrapper<TeacherDTO>> deleteTeacherById(@PathVariable Long teacherId) {
        teacherCommandService.deleteTeacher(teacherId);
        return ResponseEntity.ok(ResponseWrapper.ok(null, "Teacher successfully deleted"));
    }


    // endpoint used to validate teacher data integrity in another services
    @Operation(summary = "Validate existing teacher by ID", description = "Validates if a teacher exists by their ID.")
    @GetMapping("/{teacherId}/validate")
    public boolean validateExistingTeacherById(@PathVariable Long teacherId) {
        return teacherCommandService.validateExistingTeacher(teacherId);
    }
}
