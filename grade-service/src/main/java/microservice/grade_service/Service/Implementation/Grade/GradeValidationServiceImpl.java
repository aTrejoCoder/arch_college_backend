package microservice.grade_service.Service.Implementation.Grade;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Repository.GradeRepository;
import microservice.grade_service.Repository.GroupRepository;
import microservice.grade_service.Service.AcademicHistoryService;
import microservice.grade_service.Service.GradeValidationService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GradeValidationServiceImpl implements GradeValidationService {

    private final GradeRepository gradeRepository;
    private final AcademicHistoryService academicHistoryService;

    @Override
    public Result<Void> authorizeGradeById(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("Grade with ID " + gradeId + " not found"));;

        if (grade.getGradeStatus() == Grade.GradeStatus.VALIDATED || grade.getGradeStatus() == Grade.GradeStatus.NOT_VALID) {
            return Result.error("Grade Already Authorized");
        }

        grade.setAsAuthorized();
        gradeRepository.save(grade);

        academicHistoryService.validateGrade(grade);
        return Result.success();
    }

    @Override
    public Result<Void> validateGrade(Long gradeId) {
        return null;
    }

}
