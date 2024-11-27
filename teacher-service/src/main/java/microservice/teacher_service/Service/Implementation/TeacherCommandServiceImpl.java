package microservice.teacher_service.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import microservice.teacher_service.DTOs.TeacherInsertDTO;
import microservice.teacher_service.Mappers.TeacherMapper;
import microservice.teacher_service.Model.Teacher;
import microservice.teacher_service.Repository.TeacherRepository;
import microservice.teacher_service.Service.TeacherCommandService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherCommandServiceImpl implements TeacherCommandService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final AccountNumberService accountNumberService;


    @Override
    public void createTeacher(TeacherInsertDTO teacherInsertDTO) {
        Teacher teacher = teacherMapper.insertDtoToEntity(teacherInsertDTO);
        String accountNumber = accountNumberService.generateTeacherAccountNumber(teacher);
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
    public boolean validateExistingTeacher(String teacherAccountNumber) {
        return teacherRepository.existsByAccountNumber(teacherAccountNumber);
    }
}
