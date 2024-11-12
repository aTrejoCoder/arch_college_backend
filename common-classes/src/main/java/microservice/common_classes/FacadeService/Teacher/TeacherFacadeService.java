package microservice.common_classes.FacadeService.Teacher;

import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.CompletableFuture;

public interface TeacherFacadeService {
    CompletableFuture<Boolean> validateExisitingTeacher(Long teacherId);
    CompletableFuture<Boolean> validateExisitingTeacher(String accountNumber);
    CompletableFuture<TeacherDTO> getTeacherById(Long teacherId);
    CompletableFuture<TeacherDTO> getTeacherByAccountNumber(String accountNumber);
}
