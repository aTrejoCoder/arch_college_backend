package microservice.enrollment_service.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.common_classes.Utils.SubjectType;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;
import microservice.enrollment_service.Mappers.EnrollmentMapper;
import microservice.enrollment_service.Model.GroupEnrollment;
import microservice.enrollment_service.Repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final String schoolPeriod = SemesterData.getCurrentSchoolPeriod();

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @Override
    @Cacheable(value = "enrollmentById", key = "#enrollmentId")
    public Result<EnrollmentDTO> getEnrollmentById(Long enrollmentId) {
        Optional<GroupEnrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentId);
        return optionalEnrollment
                .map(groupEnrollment -> Result.success(enrollmentMapper.entityToDTO(groupEnrollment)))
                .orElseGet(() -> Result.error("Enrollment not found"));
    }


    @Override
    @Cacheable(value = "enrollmentByAccountNumber", key = "#studentAccountNumber")
    public List<EnrollmentDTO> getEnrollmentsByAccountNumber(String studentAccountNumber) {
        List<GroupEnrollment> currentEnrollments = enrollmentRepository.findByStudentAccountNumberAndEnrollmentPeriod(studentAccountNumber, schoolPeriod);
        return currentEnrollments.stream()
                .map(enrollmentMapper::entityToDTO)
                .toList();
    }


    @Override
    @Transactional
    public void createEnrollment(EnrollmentRelationshipDTO enrollmentRelationshipDTO, EnrollmentInsertDTO enrollmentInsertDTO) {
        StudentDTO studentDTO = enrollmentRelationshipDTO.getStudentDTO();
        ObligatorySubjectDTO obligatorySubjectDTO = enrollmentRelationshipDTO.getObligatorySubjectDTO();
        if (enrollmentRelationshipDTO.getObligatorySubjectDTO() != null) {
            ObligatorySubjectDTO subjectDTO = enrollmentRelationshipDTO.getObligatorySubjectDTO();

            GroupEnrollment groupEnrollment = GroupEnrollment.builder()
                    .enrollmentDate(LocalDateTime.now())
                    .isActive(Boolean.TRUE)
                    .groupKey(enrollmentInsertDTO.getGroupKey())
                    .studentAccountNumber(studentDTO.getAccountNumber())
                    .subjectId(subjectDTO.getId())
                    .subjectType(SubjectType.OBLIGATORY)
                    .subjectCredits(subjectDTO.getCredits())
                    .subjectKey(enrollmentInsertDTO.getSubjectKey())
                    .enrollmentPeriod(schoolPeriod)
                    .build();

            enrollmentRepository.save(groupEnrollment);

        } else  {
            throw  new RuntimeException("Only obligatory allow");
        }

    }

    @Override
    public Result<Void> deleteEnrollment(String groupKey, String subjectKey, String studentAccountNumber) {
        Optional<GroupEnrollment> optionalEnrollment = enrollmentRepository.findByGroupKeyAndSubjectKeyAndStudentAccountNumber(groupKey, subjectKey, studentAccountNumber);
        if (optionalEnrollment.isEmpty()) {
            return Result.error("Enrollment not found");
        }

        enrollmentRepository.delete(optionalEnrollment.get());

        return Result.success();
    }

    @Override
    public void deleteEnrollment(Long enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new EntityNotFoundException("GroupEnrollment with ID " + enrollmentId + " not found");
        }
        enrollmentRepository.deleteById(enrollmentId);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void expireOldEnrollments() {
        LocalDateTime expirationDate = LocalDateTime.now().minusMonths(6);
        enrollmentRepository.deactivateExpiredEnrollments(expirationDate);
    }
}
