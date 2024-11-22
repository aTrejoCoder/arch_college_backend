package microservice.grade_service.Service.Implementation;

import microservice.common_classes.Utils.Result;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Repository.GradeRepository;
import microservice.grade_service.Service.GradeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class GradeValidationServiceImpl implements GradeValidationService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeValidationServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public Result<Void> authorizeGradeById(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new NotFoundException("Grade with ID " + gradeId + " not found"));;

        if (grade.isAuthorized()) {
            return Result.error("Grade Already Authorized");
        }

        grade.removePendingStatus();
        grade.setAsAuthorized();
        gradeRepository.save(grade);

        return Result.success();
    }

    @Override
    public Result<Void> validateGradeCreation(GradeInsertDTO gradeInsertDTO) {
        List<Grade> grades = (gradeInsertDTO.getOrdinarySubjectId() != null)
                ? gradeRepository.findByStudentAccountNumberAndOrdinarySubjectId(gradeInsertDTO.getStudentAccountNumber(), gradeInsertDTO.getOrdinarySubjectId())
                : gradeRepository.findByStudentAccountNumberAndElectiveSubjectId(gradeInsertDTO.getStudentAccountNumber(), gradeInsertDTO.getElectiveSubjectId());

        if (grades.isEmpty()) {
            return Result.success(); // No subject cursed yet, grade can be created
        }

        int notApprovedCount = 0;

        for (Grade grade : grades) {
            // Look if grade is already inited
            if (grade.isPending()) {
                return Result.error("Grade for this subject is already being processes, wait until validation");
            }

            // Look if is already approved
            if (grade.isGradeApproved()) {
                return Result.error("Grade is already approved and cannot be modified.");
            } else {
                notApprovedCount++;
            }
        }

        // Validate if the student has failed the grade more than twice
        if (notApprovedCount >= 3) {
            return Result.error("Grade failed more than twice; it can only be recorded through an extraordinary process.");
        }

        return Result.success();
    }

}
