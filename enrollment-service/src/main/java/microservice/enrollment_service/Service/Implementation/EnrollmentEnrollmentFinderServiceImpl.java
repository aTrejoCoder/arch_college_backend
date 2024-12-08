package microservice.enrollment_service.Service.Implementation;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.enrollment_service.Mappers.EnrollmentMapper;
import microservice.enrollment_service.Model.Enrollment;
import microservice.enrollment_service.Repository.EnrollmentRepository;
import microservice.enrollment_service.Service.EnrollmentFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EnrollmentEnrollmentFinderServiceImpl implements EnrollmentFinderService {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final String CURRENT_SCHOOL_PERIOD = AcademicData.getCurrentSchoolPeriod();

    @Autowired
    public EnrollmentEnrollmentFinderServiceImpl(EnrollmentRepository enrollmentRepository,
                                                 EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @Override
    @Cacheable(value = "enrollmentById", key = "#enrollmentId")
    public Result<EnrollmentDTO> getById(Long enrollmentId) {
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentId);
        return optionalEnrollment
                .map(groupEnrollment -> Result.success(enrollmentMapper.entityToDTO(groupEnrollment)))
                .orElseGet(() -> Result.error("Enrollment not found"));
    }


    @Override
    @Cacheable(value = "enrollmentByAccountNumber", key = "#studentAccountNumber")
    public List<EnrollmentDTO> getByAccountNumber(String studentAccountNumber) {
        List<Enrollment> currentEnrollments = enrollmentRepository.findByStudentAccountNumberAndSchoolPeriod(studentAccountNumber, CURRENT_SCHOOL_PERIOD);
        return currentEnrollments.stream()
                .map(enrollmentMapper::entityToDTO)
                .toList();
    }
}
