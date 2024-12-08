package microservice.teacher_service.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.teacher_service.DTOs.TeacherInsertDTO;
import microservice.teacher_service.Mappers.TeacherMapper;
import microservice.teacher_service.Messaging.TeacherMessageSender;
import microservice.teacher_service.Model.Teacher;
import microservice.teacher_service.Repository.TeacherRepository;
import microservice.teacher_service.Service.TeacherCommandService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherCommandServiceImpl implements TeacherCommandService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final AccountNumberService accountNumberService;
    private final TeacherMessageSender teacherMessageSender;

    @Override
    public void createTeacher(TeacherInsertDTO teacherInsertDTO) {
        Teacher teacher = teacherMapper.insertDtoToEntity(teacherInsertDTO);
        String accountNumber = accountNumberService.generateTeacherAccountNumber(teacher);
        teacher.setAccountNumber(accountNumber);

        teacherRepository.save(teacher);
        log.info("Teacher created with Account Number: {}", teacher.getAccountNumber());

        teacherMessageSender.sendTeacherCreation(teacher);
        log.info("Teacher creation message sent for account number: {}", teacher.getAccountNumber());
    }

    @Override
    public void deleteTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + teacherId + " not found"));

        teacherRepository.delete(teacher);
        log.info("Teacher with ID {} deleted successfully: {}", teacherId, teacher);

        teacherMessageSender.sendTeacherDeletion(teacher.getAccountNumber());
        log.info("Teacher deletion message sent for account number: {}", teacher.getAccountNumber());
    }

    @Override
    public boolean validateExistingTeacher(String teacherAccountNumber) {
        return teacherRepository.existsByAccountNumber(teacherAccountNumber);
    }
}
