package microservice.student_service.messaging;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Grade.InitAcademicHistory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQSender {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void queueAcademicHistoryInit(InitAcademicHistory initAcademicHistory) {
        log.info("queueing new academic history for student with account number {}", initAcademicHistory.getStudent().getAccountNumber());
        rabbitTemplate.convertAndSend("academic.history.exchange", "academic.history.create", initAcademicHistory);
    }
}
