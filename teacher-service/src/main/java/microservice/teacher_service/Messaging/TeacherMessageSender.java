package microservice.teacher_service.Messaging;

import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.teacher_service.Mappers.TeacherMapper;
import microservice.teacher_service.Model.Teacher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final TeacherMapper teacherMapper;

    public void sendTeacherCreation(Teacher teacher) {
        TeacherDTO teacherDTO = teacherMapper.entityToDTO(teacher);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TEACHER_EXCHANGE,
                RabbitMQConfig.TEACHER_CREATE_ROUTING_KEY,
                teacherDTO
        );
    }

    public void sendTeacherDeletion(String teacherAccountNumber) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TEACHER_EXCHANGE,
                RabbitMQConfig.TEACHER_DELETE_ROUTING_KEY,
                teacherAccountNumber
        );
    }

}
