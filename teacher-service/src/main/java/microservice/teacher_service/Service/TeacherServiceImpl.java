package microservice.teacher_service.Service;

import jakarta.persistence.EntityNotFoundException;
import microservice.common_classes.Utils.Result;
import microservice.teacher_service.DTOs.TeacherDTO;
import microservice.teacher_service.DTOs.TeacherInsertDTO;
import microservice.teacher_service.Mappers.TeacherMapper;
import microservice.teacher_service.Model.Teacher;
import microservice.teacher_service.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final TeacherDomainService teacherDomainService;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository,
                              TeacherMapper teacherMapper,
                              TeacherDomainService teacherDomainService) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
        this.teacherDomainService = teacherDomainService;
    }

    @Override
    @Cacheable(value = "teacherById", key = "#teacherId")
    public Result<TeacherDTO> getTeacherById(Long teacherId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        return optionalTeacher
                .map(teacher -> Result.success(teacherMapper.entityToDTO(teacher)))
                .orElseGet(() -> Result.error("Teacher not found"));
    }

    @Override
    @Cacheable(value = "teachersByLastname", key = "{#pageable.pageNumber, #pageable.pageSize, #sortDirection}")
    public Page<TeacherDTO> getAllTeachersSortedByLastname(Pageable pageable, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection.toUpperCase()).orElse(Sort.Direction.ASC);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "lastName"));

        Page<Teacher> teachers = teacherRepository.findAll(sortedPageable);
        return teachers.map(teacherMapper::entityToDTO);
    }

    @Override
    @Cacheable(value = "teachersByAccountNumber", key = "{#pageable.pageNumber, #pageable.pageSize, #sortDirection}")
    public Page<TeacherDTO> getAllTeachersSortedByAccountNumber(Pageable pageable, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection.toUpperCase()).orElse(Sort.Direction.ASC);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "accountNumber"));

        Page<Teacher> teachers = teacherRepository.findAll(sortedPageable);
        return teachers.map(teacherMapper::entityToDTO);
    }

    @Override
    @Cacheable(value = "teachersByAccountNumber", key = "{#pageable.pageNumber, #pageable.pageSize, #sortDirection}")
    public Page<TeacherDTO> getAllTeachersSortedByTitle(Pageable pageable, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection.toUpperCase()).orElse(Sort.Direction.ASC);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "title"));

        Page<Teacher> teachers = teacherRepository.findAll(sortedPageable);
        return teachers.map(teacherMapper::entityToDTO);
    }

    @Override
    @Cacheable(value = "teacherByAccountNumber", key = "#accountNumber")
    public Result<TeacherDTO> getTeacherByAccountNumber(String accountNumber) {
        Optional<Teacher> optionalTeacher = teacherRepository.findByAccountNumber(accountNumber);
        return optionalTeacher
                .map(teacher -> Result.success(teacherMapper.entityToDTO(teacher)))
                .orElseGet(() -> Result.error("Teacher not found"));
    }


    @Override
    @Cacheable(value = "teacherByTitle", key = "#title")
    public Page<TeacherDTO> getTeachersByTitle(String title, Pageable pageable) {
        Page<Teacher> teacherPage = teacherRepository.findByTitle(title, pageable);
        return teacherPage.map(teacherMapper::entityToDTO);
    }


    @Override
    public void createTeacher(TeacherInsertDTO teacherInsertDTO) {
        Teacher teacher = teacherMapper.insertDtoToEntity(teacherInsertDTO);
        String accountNumber = teacherDomainService.generateTeacherAccountNumber(teacher);
        teacher.setAccountNumber(accountNumber);

        teacherRepository.save(teacher);
    }

    @Override
    public void updateTeacher(TeacherInsertDTO teacherInsertDTO, Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + teacherId + " not found"));

        teacherMapper.updatePersonalData(teacher, teacherInsertDTO);
        teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(Long teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new EntityNotFoundException("Entity with ID " + teacherId + " not found");
        }
        teacherRepository.deleteById(teacherId);
    }

    @Override
    public boolean validateExistingTeacher(Long teacherId) {
        return teacherRepository.existsById(teacherId);
    }
}
