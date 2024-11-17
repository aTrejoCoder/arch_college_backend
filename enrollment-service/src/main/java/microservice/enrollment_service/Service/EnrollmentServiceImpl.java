package microservice.enrollment_service.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.Utils.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.enrollment_service.DTOs.EnrollmentDTO;
import microservice.enrollment_service.DTOs.EnrollmentInsertDTO;
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
    private final String schoolPeriod = SemesterData.getCurrentSemester();

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
                .orElseGet(() -> Result.error("GroupEnrollment not found"));
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
    public void createEnrollment(EnrollmentInsertDTO enrollmentInsertDTO) {
        GroupEnrollment groupEnrollment = enrollmentMapper.insertDtoToEntity(enrollmentInsertDTO);
        enrollmentRepository.save(groupEnrollment);
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
