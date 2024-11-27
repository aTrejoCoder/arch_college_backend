package microservice.grade_service.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.JWT.JWTSecurity;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.grade_service.Model.AcademicHistory;
import microservice.grade_service.Repository.GradeRepository;
import microservice.grade_service.Service.AcademicHistoryService;
import microservice.grade_service.Service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/api/students")
@RequiredArgsConstructor
public class StudentAcademicHistoryController {

    private final AcademicHistoryService academicHistoryService;
    private final GradeService gradeService;
    private final JWTSecurity jwtSecurity;

    @GetMapping("/get-my-academic-history")
    public ResponseEntity<ResponseWrapper<AcademicHistory>> getMyAcademicHistory(HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        AcademicHistory academicHistory = academicHistoryService.getAcademicHistoryByAccountNumber(accountNumber);

        return ResponseEntity.ok(ResponseWrapper.found(academicHistory, "Academic History"));
    }

    @GetMapping("/get-my-annually-grades")
    public ResponseEntity<ResponseWrapper<List<GradeDTO>>> getMyYearEnrollments(HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        List<GradeDTO> annuallyGrades = gradeService.getAnnuallyGradesByStudentAccountNumber(accountNumber);

        return ResponseEntity.ok(ResponseWrapper.found(annuallyGrades, "Annually Grades"));
    }

    @GetMapping("/get-my-current-enrollments")
    public ResponseEntity<ResponseWrapper<List<GradeDTO>>> getMyCurrentEnrollments(HttpServletRequest request) {
        String accountNumber = jwtSecurity.getAccountNumberFromToken(request);

        List<GradeDTO> currentEnrollments = gradeService.getCurrentGradesByStudentAccountNumber(accountNumber);

        return ResponseEntity.ok(ResponseWrapper.found(currentEnrollments, "Current Enrollments"));
    }
}
