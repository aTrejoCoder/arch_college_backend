package microservice.student_service.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Student.StudentInsertDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;
import microservice.common_classes.Utils.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.student_service.Mappers.StudentMapper;
import microservice.student_service.Model.Student;
import microservice.student_service.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final AccountNumberGenerationService accountNumberGenerationService;
    private final String currentGenerationIncome =  SemesterData.getCurrentSemester();

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              StudentMapper studentMapper,
                              AccountNumberGenerationService accountNumberGenerationService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.accountNumberGenerationService = accountNumberGenerationService;
    }

    @Override
    @Cacheable(value = "studentById", key = "#studentId")
    public Result<StudentDTO> getStudentById(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        return optionalStudent
                .map(student -> Result.success(studentMapper.entityToDTO(student)))
                .orElseGet(() -> Result.error("Student not found"));
    }

    @Override
    @Cacheable(value = "studentsByLastname", key = "{#pageable.pageNumber, #pageable.pageSize, #sortDirection}")
    public Page<StudentDTO> getAllStudentsSortedByLastname(Pageable pageable, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection.toUpperCase()).orElse(Sort.Direction.ASC);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "lastName"));

        Page<Student> students = studentRepository.findAll(sortedPageable);
        return students.map(studentMapper::entityToDTO);
    }

    @Override
    @Cacheable(value = "studentsByAccountNumber", key = "{#pageable.pageNumber, #pageable.pageSize, #sortDirection}")
    public Page<StudentDTO> getAllStudentsSortedByAccountNumber(Pageable pageable, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection.toUpperCase()).orElse(Sort.Direction.ASC);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "accountNumber"));

        Page<Student> students = studentRepository.findAll(sortedPageable);
        return students.map(studentMapper::entityToDTO);
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
    @Transactional
    public StudentDTO createStudent(StudentInsertDTO studentInsertDTO) {
        Student student = studentMapper.insertDtoToEntity(studentInsertDTO);
        String accountNumber = accountNumberGenerationService.generateStudentAccountNumber(student);

        student.initializeAcademicValues(accountNumber, currentGenerationIncome);
        studentRepository.saveAndFlush(student);

        return studentMapper.entityToDTO(student);
    }

    @Override
    public void updateStudent(StudentInsertDTO studentInsertDTO, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + studentId + " not found"));

        studentMapper.updatePersonalData(student, studentInsertDTO);
        studentRepository.save(student);
    }

    public void setProfessionalLineData(String accountNumber, Long professionalLineId, ProfessionalLineModality professionalLineModality) {
        Student student = studentRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Entity with Account Number " + accountNumber + " not found"));

        if (student.getSemestersCompleted() < 6) {
            throw  new RuntimeException("Student not able to set professional line data");
        }

        student.setProfessionalLineId(professionalLineId);
        student.setProfessionalLineModality(professionalLineModality);

        studentRepository.save(student);
    }

    public void increaseSemestersCursed(String accountNumber) {
        Student student = studentRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Entity with Account Number " + accountNumber + " not found"));

        student.increaseSemesterCompleted();

        studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Entity with ID " + studentId + " not found");
        }
        studentRepository.deleteById(studentId);
    }

    @Override
    public boolean validateExistingStudent(Long studentId) {
        return studentRepository.existsById(studentId);
    }
}
