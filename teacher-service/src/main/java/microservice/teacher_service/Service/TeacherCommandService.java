package microservice.teacher_service.Service;

import microservice.teacher_service.DTOs.TeacherInsertDTO;

public interface TeacherCommandService {
    void createTeacher(TeacherInsertDTO teacherInsertDTO);
    void deleteTeacher(Long studentId);

    boolean validateExistingTeacher(String teacherAccountNumber);
}
