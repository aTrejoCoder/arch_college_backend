package microservice.grade_service.Messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentGradeDTO;
import microservice.common_classes.DTOs.Enrollment.GroupEnrollmentDTO;
import microservice.grade_service.Model.Group;
import microservice.grade_service.Model.Subject;
import microservice.grade_service.Service.GradeCommandService;
import microservice.grade_service.Service.GroupService;
import microservice.grade_service.Service.Implementation.SubjectService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnrollmentReceiver {

    private final ObjectMapper objectMapper;
    private final SubjectService subjectService;
    private final GroupService groupService;
    private final GradeCommandService gradeCommandService;

    @RabbitListener(queues = RabbitMQConfig.EG_QUEUE)
    public void receiveGroupEnrollment(GroupEnrollmentDTO groupEnrollmentDTO) {
        try {
            log.info("receiveEnrollment -> receiving {} from group ID {}", groupEnrollmentDTO.getEnrollments().size(), groupEnrollmentDTO.getGroupId());
            Subject subject = subjectService.ensureSubjectsExist(groupEnrollmentDTO);
            Group group = groupService.createGroupFromEnrollment(groupEnrollmentDTO, subject);
            gradeCommandService.initGradesFromEnrollments(group, groupEnrollmentDTO.getEnrollments());
            log.info("receiveEnrollment -> grades successfully processed from group ID {}", groupEnrollmentDTO.getGroupId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

