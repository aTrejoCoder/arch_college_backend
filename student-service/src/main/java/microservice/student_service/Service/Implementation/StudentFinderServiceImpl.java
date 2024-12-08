package microservice.student_service.Service.Implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.common_classes.Utils.Student.StudentFilter;
import microservice.student_service.Mappers.StudentMapper;
import microservice.student_service.Model.Student;
import microservice.student_service.Repository.StudentRepository;
import microservice.student_service.Service.StudentFinderService;
import microservice.student_service.Utils.StudentSpecifications;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentFinderServiceImpl implements StudentFinderService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final String currentGenerationIncome =  AcademicData.getCurrentSchoolPeriod();

    @Override
    @Cacheable(value = "studentById", key = "#studentId")
    public Result<StudentDTO> getStudentById(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        return optionalStudent
                .map(student -> Result.success(studentMapper.entityToDTO(student)))
                .orElseGet(() -> Result.error("Student not found"));
    }

    @Override
    @Cacheable(value = "studentByAccountNumber", key = "#accountNumber")
    public Result<StudentDTO> getStudentByAccountNumber(String accountNumber) {
        Optional<Student> optionalStudent = studentRepository.findByAccountNumber(accountNumber);
        return optionalStudent
                .map(student -> Result.success(studentMapper.entityToDTO(student)))
                .orElseGet(() -> Result.error("Student not found"));
    }

    @Override
    public Page<StudentDTO> getStudentsSortedByFilterPageable(Pageable pageable, StudentFilter filter, String param) {
        Specification<Student> specification = StudentSpecifications.withFilter(filter, param);

        Page<Student> students = studentRepository.findAll(specification, pageable);

        return students.map(studentMapper::entityToDTO);
    }

    @Override
    public Page<StudentDTO> getAllStudentsSortedByFilterPageable(Pageable pageable, StudentFilter filter) {
        Specification<Student> specification = StudentSpecifications.withFilter(filter);
        Page<Student> students = studentRepository.findAll(specification, pageable);

        return students.map(studentMapper::entityToDTO);
    }
}
