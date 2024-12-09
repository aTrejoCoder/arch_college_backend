package microservice.grade_service.Controller.TeacherGrade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.JWT.JWTSecurity;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.DTOs.GroupDTO;
import microservice.grade_service.DTOs.TeacherQualificationDTO;
import microservice.grade_service.Service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("v1/api/teachers/grades/groups")
@RequiredArgsConstructor
@Tag(name = "Teacher Grade Controller", description = "APIs to manage teacher grading actions")
public class TeacherGradeController {

    private final GroupService groupService;
    private final JWTSecurity jwtSecurity;

    @Operation(
            summary = "Get pending groups to grade",
            description = "Returns a list of groups where the teacher has pending grades to assign.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved groups pending grading",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseWrapper.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    @GetMapping("/pending-grade")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getMyGroupsToBeQualified(
            @Parameter(hidden = true) HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        List<GroupDTO> groups = groupService.getTeacherGroupsPendingToBeQualified(accountNumber);
        return ResponseEntity.ok(ResponseWrapper.found(groups, "Groups"));
    }

    @Operation(
            summary = "Get graded groups history",
            description = "Returns a list of groups where the teacher has completed grading.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved graded groups history",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseWrapper.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    @GetMapping("/my-history")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getMyGroupsGradedHistory(
            @Parameter(hidden = true) HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        List<GroupDTO> groups = groupService.getTeacherGroupsQualified(accountNumber);

        return ResponseEntity.ok(ResponseWrapper.found(groups, "Groups"));
    }

    @Operation(
            summary = "Assign grades to a group",
            description = "Allows a teacher to assign grades to a specific group.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details about the grades to assign, including the group ID and grade values",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TeacherQualificationDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grades successfully assigned",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseWrapper.class))),
                    @ApiResponse(responseCode = "400", description = "Validation failed for the grading period or group qualification"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    @PutMapping("/set-grades")
    public ResponseEntity<ResponseWrapper<Void>> putGradeValuesToGroup(
            @Valid @Parameter(hidden = true) HttpServletRequest request,
            @RequestBody TeacherQualificationDTO teacherQualificationDTO) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);
        log.info("Teacher [{}] initiated request to assign grades for group ID [{}].", accountNumber, teacherQualificationDTO.getGroupId());

        Result<Void> periodTimeResult = groupService.validateGroupGradingPeriodTime();
        if (periodTimeResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(periodTimeResult.getErrorMessage()));
        }

        Result<Void> validationResult = groupService.validateGroupQualification(teacherQualificationDTO, accountNumber);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(validationResult.getErrorMessage()));
        }

        groupService.addGroupQualifications(teacherQualificationDTO, accountNumber);
        groupService.addGradesToAcademicHistoryAsync(teacherQualificationDTO.getGroupId());
        log.info("Teacher [{}] successfully assigned grades to group ID [{}].", accountNumber, teacherQualificationDTO.getGroupId());

        return ResponseEntity.ok(ResponseWrapper.ok("Group Grade Values Successfully Added."));
    }

}
