package microservice.grade_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.DTOs.Grade.InitAcademicHistory;
import microservice.grade_service.Model.AcademicHistory;
import microservice.grade_service.Service.AcademicHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/academic-histories")
public class AcademicHistoryController {

    private final AcademicHistoryService academicHistoryService;

    @Autowired
    public AcademicHistoryController(AcademicHistoryService academicHistoryService) {
        this.academicHistoryService = academicHistoryService;
    }

    @PostMapping("/init")
    public ResponseEntity<Void> initAcademicHistory(@Valid @RequestBody InitAcademicHistory initAcademicHistory) {
        academicHistoryService.validateUniqueAcademicHistoryPerStudent(initAcademicHistory.getStudent().getAccountNumber());

        academicHistoryService.initAcademicHistory(initAcademicHistory.getStudent(), initAcademicHistory.getCareer());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/student/{accountNumber}")
    public ResponseEntity<ResponseWrapper<AcademicHistory>> getAcademicHistoryByStudentAccountNumber(@Valid @PathVariable String accountNumber) {

         AcademicHistory academicHistory = academicHistoryService.getAcademicHistoryByAccountNumber(accountNumber);
        return ResponseEntity.ok(ResponseWrapper.found(academicHistory, "Academic History", "account number",  accountNumber));
    }
}
