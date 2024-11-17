package microservice.common_classes.FacadeService.Teacher;

import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.common_classes.Utils.Result;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface TeacherFacadeService {
    CompletableFuture<Boolean> validateExisitingTeacher(Long teacherId);
    CompletableFuture<Boolean> validateExisitingTeacher(String accountNumber);
    CompletableFuture<TeacherDTO> getTeacherById(Long teacherId);
    CompletableFuture<Result<List<TeacherDTO>>> getTeachersById(Set<Long> teacherIdSet);
    CompletableFuture<TeacherDTO> getTeacherByAccountNumber(String accountNumber);
}
