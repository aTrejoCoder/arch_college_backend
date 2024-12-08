package microservice.enrollment_service.Messaging.RabbitMQConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.Utils.SubjectType;
import microservice.common_classes.DTOs.Enrollment.GroupEnrollmentDTO;
import microservice.enrollment_service.Mappers.EnrollmentMapper;
import microservice.enrollment_service.Model.Enrollment;
import microservice.enrollment_service.Model.Preload.ElectiveSubject;
import microservice.enrollment_service.Model.Preload.Group;
import microservice.enrollment_service.Model.Preload.ObligatorySubject;
import microservice.enrollment_service.Repository.ElectiveSubjectRepository;
import microservice.enrollment_service.Repository.GroupRepository;
import microservice.enrollment_service.Repository.ObligatorySubjectRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnrollmentGradeProducer {

    private final RabbitTemplate rabbitTemplate;
    private final EnrollmentMapper enrollmentMapper;
    private final GroupRepository groupRepository;
    private final ObligatorySubjectRepository obligatorySubjectRepository;
    private final ElectiveSubjectRepository electiveSubjectRepository;

    private void sendGroupEnrollments(GroupEnrollmentDTO groupEnrollment) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_GRADE,
                RabbitMQConfig.ROUTING_KEY,
                groupEnrollment
        );
        log.info("sendEnrollments -> sending enrollmentS for group with ID {}", groupEnrollment.getGroupId());
    }

    public void sendEnrollmentsToGradeService(List<Enrollment> enrollments) {
        Set<Long> groupIds = enrollments.stream()
                .mapToLong(Enrollment::getGroupId)
                .boxed()
                .collect(Collectors.toSet());

        for (var groupId : groupIds) {
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new NotFoundException("Group " + groupId + " not found"));

            GroupEnrollmentDTO groupEnrollmentDTO = GroupEnrollmentDTO.builder()
                    .groupType(group.getGroupType())
                    .subjectId(group.getSubjectId())
                    .subjectType(group.getSubjectType())
                    .groupId(group.getId())
                    .subjectName(group.getSubjectName())
                    .headTeacherAccountNumber(group.getHeadTeacherAccountNumber())
                    .schoolPeriod(group.getSchoolPeriod())
                    .build();

            if (group.getSubjectType() == SubjectType.OBLIGATORY) {
                ObligatorySubject obligatorySubject = obligatorySubjectRepository.findByKey(group.getSubjectKey())
                        .orElseThrow(() -> new NotFoundException("Obligatory Subject with Key " + group.getSubjectKey() + " not found"));

                groupEnrollmentDTO.setSubjectCredits(obligatorySubject.getCredits());

            } else {
                ElectiveSubject electiveSubject = electiveSubjectRepository.findByKey(group.getSubjectKey())
                        .orElseThrow(() -> new NotFoundException("Elective Subject with Key " + group.getSubjectKey() + " not found"));

                groupEnrollmentDTO.setSubjectCredits(electiveSubject.getCredits());
            }

            List<EnrollmentDTO> groupEnrollments = enrollments.stream()
                    .filter(enrollment -> enrollment.getGroupId().equals(groupId))
                    .map(enrollmentMapper::entityToDTO)
                    .toList();

            groupEnrollmentDTO.setEnrollments(groupEnrollments);

            log.info("sendGroupEnrollments -> sending DTO {}", groupEnrollmentDTO);
            sendGroupEnrollments(groupEnrollmentDTO);
        }
    }
}

