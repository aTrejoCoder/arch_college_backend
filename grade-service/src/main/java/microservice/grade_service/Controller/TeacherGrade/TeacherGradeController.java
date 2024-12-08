package microservice.grade_service.Controller.TeacherGrade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("v1/api/grades/teachers/groups")
@RequiredArgsConstructor
public class TeacherGradeController {

    private final GroupService groupService;
    private final JWTSecurity jwtSecurity;

    @GetMapping("/pending-grade")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getMyGroupsToBeQualified(HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        List<GroupDTO> groups = groupService.getTeacherGroupsPendingToBeQualified(accountNumber);
        return ResponseEntity.ok(ResponseWrapper.found(groups, "Groups"));
    }

    @GetMapping("/my-history")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getMyGroupsGradedHistory(HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        List<GroupDTO> groups = groupService.getTeacherGroupsQualified(accountNumber);

        return ResponseEntity.ok(ResponseWrapper.found(groups, "Groups"));
    }

    @PutMapping("/set-grades")
    public ResponseEntity<ResponseWrapper<Void>> putGradeValuesToGroup(@Valid HttpServletRequest request,
                                                                       @RequestBody TeacherQualificationDTO teacherQualificationDTO) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

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

        return ResponseEntity.ok(ResponseWrapper.ok("Group Grade Values Successfully Added."));
    }
}
