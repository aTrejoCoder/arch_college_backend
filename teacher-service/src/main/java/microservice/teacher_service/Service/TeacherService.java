package microservice.teacher_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.teacher_service.DTOs.TeacherDTO;
import microservice.teacher_service.DTOs.TeacherInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeacherService {
    Result<TeacherDTO> getTeacherById(Long studentId);
    Result<TeacherDTO> getTeacherByAccountNumber(String accountNumber);
    Page<TeacherDTO> getTeachersByTitle(String title, Pageable pageable);
    Page<TeacherDTO> getAllTeachersSortedByLastname(Pageable pageable, String sortDirection);
    Page<TeacherDTO> getAllTeachersSortedByAccountNumber(Pageable pageable, String sortDirection);
    Page<TeacherDTO> getAllTeachersSortedByTitle(Pageable pageable, String sortDirection);

    void createTeacher(TeacherInsertDTO teacherInsertDTO);
    void updateTeacher(TeacherInsertDTO teacherInsertDTO, Long studentId);
    void deleteTeacher(Long studentId);

    boolean validateExistingTeacher(Long studentId);
}
