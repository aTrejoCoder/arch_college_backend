package microservice.common_classes.FacadeService;

import microservice.common_classes.DTOs.Teacher.TeacherDTO;

public interface TeacherFacadeService {
    boolean validateExisitingTeacher(Long teacherId);
    TeacherDTO getTeacherById(Long teacherId);
}
