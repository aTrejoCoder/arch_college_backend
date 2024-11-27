package microservice.teacher_service.Service.Implementation;

import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Teacher.Title;
import microservice.teacher_service.Mappers.TeacherMapper;
import microservice.teacher_service.Model.Teacher;
import microservice.teacher_service.Repository.TeacherRepository;
import microservice.teacher_service.Service.TeacherFinderService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherFinderServiceImpl implements TeacherFinderService {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;

    @Override
    @Cacheable(value = "teacherById", key = "#teacherId")
    public Result<TeacherDTO> getTeacherById(Long teacherId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        return optionalTeacher
                .map(teacher -> Result.success(teacherMapper.entityToDTO(teacher)))
                .orElseGet(() -> Result.error("Teacher not found"));
    }


    @Override
    public Result<List<TeacherDTO>> getTeachersByIds(Set<Long> idSet) {
        List<Teacher> teachers = teacherRepository.findByIdIn(idSet);

        Set<Long> idsFounded = teachers.stream()
                .map(Teacher::getId)
                .collect(Collectors.toSet());

        Set<Long> missingIds = idSet.stream()
                .filter(id -> !idsFounded.contains(id))
                .collect(Collectors.toSet());

        if (!missingIds.isEmpty()) {
            String errorMessage = "Teachers not found for IDs: " + missingIds;
            return Result.error(errorMessage);
        }

        List<TeacherDTO> teacherDTOS = teachers.stream().map(teacherMapper::entityToDTO).toList();

        return Result.success(teacherDTOS);
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
    public Page<TeacherDTO> getAllTeachersSorted(Pageable pageable, String sortDirection, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return teacherRepository.findAll(sortedPageable)
                .map(teacherMapper::entityToDTO);
    }

    @Override
    @Cacheable(value = "teacherByTitle", key = "#title")
    public Page<TeacherDTO> getTeachersByTitlePageable(Title title, Pageable pageable) {
        Page<Teacher> teacherPage = teacherRepository.findByTitle(title, pageable);
        return teacherPage.map(teacherMapper::entityToDTO);
    }
}
