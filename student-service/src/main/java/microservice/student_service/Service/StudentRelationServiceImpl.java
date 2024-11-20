package microservice.student_service.Service;

import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentRelationServiceImpl implements StudentRelationService {

    private final SubjectFacadeService subjectFacadeService;

    @Autowired
    public StudentRelationServiceImpl(SubjectFacadeService subjectFacadeService) {
        this.subjectFacadeService = subjectFacadeService;
    }

    @Override
    public Result<Void> validateExistingCareerId(Long careerId) {
        CareerDTO careerDTO = subjectFacadeService.getCareerById(careerId).join();
        if (careerDTO == null) {
            return Result.error("Career with ID" + careerId + " not found" );
        }

        return Result.success();
    }

    @Override
    public Result<Void> validateProfessionalLineId(Long professionalLineId) {
        ProfessionalLineDTO professionalLineDTO = subjectFacadeService.getProfessionalLineById(professionalLineId).join();
        if (professionalLineDTO == null) {
            return Result.error("Professional Line with ID" + professionalLineId + " not found" );
        }

        return Result.success();
    }
}
