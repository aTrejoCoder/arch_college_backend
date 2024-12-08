package microservice.grade_service.Service;

import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.EnrollmentGradeDTO;
import microservice.common_classes.DTOs.Enrollment.GroupEnrollmentDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.DTOs.GroupDTO;
import microservice.grade_service.DTOs.TeacherQualificationDTO;
import microservice.grade_service.Model.Group;
import microservice.grade_service.Model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {
    Result<GroupDTO> getGroupById(Long groupId);
    Page<GroupDTO> getPendingGroups(Pageable pageable);
    List<GroupDTO> getTeacherGroupsPendingToBeQualified(String teacherAccountNumber);
    List<GroupDTO> getTeacherGroupsQualified(String teacherAccountNumber);

    Result<Void> validateGroupQualification(TeacherQualificationDTO teacherQualificationDTO, String teacherAccountNumber);
    Result<Void> validateGroupGradingPeriodTime();

    Group createGroupFromEnrollment(GroupEnrollmentDTO groupEnrollmentDTO, Subject subject);
    void addGroupQualifications(TeacherQualificationDTO teacherQualificationDTO, String teacherAccountNumber);
    void undoGroupQualifications(Long groupId, String teacherAccountNumber);

    void addGradesToAcademicHistoryAsync(Long groupId);
}
