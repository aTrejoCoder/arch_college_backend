package microservice.enrollment_service.Service.Implementation;

import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Enrollment.EnrollmentGradeDTO;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.enrollment_service.Messaging.RabbitMQConfig.EnrollmentGradeProducer;
import microservice.enrollment_service.Model.Enrollment;
import microservice.enrollment_service.Model.Preload.Grade;
import microservice.enrollment_service.Model.Preload.Group;
import microservice.enrollment_service.Repository.EnrollmentRepository;
import microservice.enrollment_service.Repository.GroupRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentLockService {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentGradeProducer enrollmentGradeProducer;
    private static final String schoolPeriod = AcademicData.getCurrentSchoolPeriod();

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleLockEnrollments() {
        LocalDateTime targetDate = getLockDate();

        if (LocalDateTime.now().isAfter(targetDate)) {
            lockEnrollments();
        }
    }

    public LocalDateTime getLockDate() {
        return AcademicData.getCurrentSchoolPeriodStartDate().plusMonths(2);
    }

    private void lockEnrollments() {
        List<Enrollment> currentEnrollments = enrollmentRepository.findBySchoolPeriod(schoolPeriod);
        List<Enrollment> currentEnrollmentsLocked = currentEnrollments.stream()
                .peek(Enrollment::lock)
                .toList();

        enrollmentRepository.saveAll(currentEnrollmentsLocked);

        enrollmentGradeProducer.sendEnrollmentsToGradeService(currentEnrollments);
    }
}
