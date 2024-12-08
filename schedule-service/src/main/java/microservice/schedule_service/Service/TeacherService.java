package microservice.schedule_service.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.schedule_service.Mapppers.TeacherMapper;
import microservice.schedule_service.Models.Teacher;
import microservice.schedule_service.Repository.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;

    public void createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.dtoToEntity(teacherDTO);
        teacherRepository.save(teacher);
    }


    public void deleteTeacher(String teacherAccountNumber) {
        Teacher teacher = teacherRepository.findByAccountNumber(teacherAccountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Can't Delete Teacher with Account Number " + teacherAccountNumber + " . Teacher not found"));

        teacherRepository.delete(teacher);
    }

}
