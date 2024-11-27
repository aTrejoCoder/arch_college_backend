package microservice.grade_service.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.DTOs.Grade.InitAcademicHistory;
import microservice.grade_service.Model.AcademicHistory;
import microservice.grade_service.Service.AcademicHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/academic-histories")
@RequiredArgsConstructor
public class AcademicHistoryController {

    private final AcademicHistoryService academicHistoryService;

    @PostMapping("/student/{accountNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<AcademicHistory>> getAcademicHistoryByStudentAccountNumber(@Valid @PathVariable String accountNumber) {

         AcademicHistory academicHistory = academicHistoryService.getAcademicHistoryByAccountNumber(accountNumber);
        return ResponseEntity.ok(ResponseWrapper.found(academicHistory, "Academic History", "account number",  accountNumber));
    }
}
