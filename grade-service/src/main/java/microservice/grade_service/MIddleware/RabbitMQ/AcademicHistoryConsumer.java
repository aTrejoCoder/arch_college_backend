package microservice.grade_service.MIddleware.RabbitMQ;

import microservice.grade_service.Config.RabbitMQConfig;
import microservice.grade_service.Service.AcademicHistoryService;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Grade.InitAcademicHistory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AcademicHistoryConsumer {

    private final AcademicHistoryService academicHistoryService;

    @Autowired
    public AcademicHistoryConsumer(AcademicHistoryService academicHistoryService) {
        this.academicHistoryService = academicHistoryService;
    }

    @RabbitListener(queues = RabbitMQConfig.ACADEMIC_HISTORY_QUEUE)
    public void processAcademicHistory(InitAcademicHistory initAcademicHistory) {
        log.info("Processing message to create academic history for user with account number: {}", initAcademicHistory.getStudent().getAccountNumber());
        academicHistoryService.validateUniqueAcademicHistoryPerStudent(initAcademicHistory.getStudent().getAccountNumber());

        academicHistoryService.initAcademicHistory(initAcademicHistory.getStudent(), initAcademicHistory.getCareer());
        log.info("Academic History created for student: {}", initAcademicHistory.getStudent().getAccountNumber());
    }
}