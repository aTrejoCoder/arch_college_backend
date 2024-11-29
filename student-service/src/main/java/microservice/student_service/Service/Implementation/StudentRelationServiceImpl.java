package microservice.student_service.Service.Implementation;

import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Grade.InitAcademicHistory;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.Utils.Response.Result;
import microservice.student_service.Service.StudentRelationService;
import microservice.student_service.messaging.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class StudentRelationServiceImpl implements StudentRelationService {

    private final SubjectFacadeService subjectFacadeService;
    private final RabbitMQSender rabbitMQSender;

    @Autowired
    public StudentRelationServiceImpl(@Qualifier("SubjectFacadeServiceImpl") SubjectFacadeService subjectFacadeService,
                                      RabbitMQSender rabbitMQSender) {
        this.subjectFacadeService = subjectFacadeService;
        this.rabbitMQSender = rabbitMQSender;
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


    @Override
    @Async("taskExecutor")
    public void initAcademicHistoryAsync(StudentDTO studentDTO) {
         CompletableFuture.runAsync(() -> {
            CareerDTO careerDTO = subjectFacadeService.getCareerById(studentDTO.getCareerId()).join();

            InitAcademicHistory initAcademicHistory = new InitAcademicHistory(studentDTO, careerDTO);

            rabbitMQSender.queueAcademicHistoryInit(initAcademicHistory);
        });
    }
}
