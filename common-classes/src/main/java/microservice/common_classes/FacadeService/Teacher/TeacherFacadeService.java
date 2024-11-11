package microservice.common_classes.FacadeService.Teacher;

import microservice.common_classes.DTOs.Teacher.TeacherDTO;

import java.util.concurrent.CompletableFuture;

public interface TeacherFacadeService {
    CompletableFuture<Boolean> validateExisitingTeacher(Long teacherId);
    CompletableFuture<TeacherDTO> getTeacherById(Long teacherId);
}
