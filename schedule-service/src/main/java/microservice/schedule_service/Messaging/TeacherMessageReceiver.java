package microservice.schedule_service.Messaging;


import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.schedule_service.Service.TeacherService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherMessageReceiver {

    private final TeacherService teacherService;

    @RabbitListener(queues = RabbitMQConfig.TEACHER_QUEUE)
    public void receiveTeacher(Object message) {
        if (message instanceof TeacherDTO teacherDTO) {
            teacherService.createTeacher(teacherDTO);
        } else if (message instanceof String teacherAccountNumber) {
            teacherService.deleteTeacher(teacherAccountNumber);
        }
    }
}

