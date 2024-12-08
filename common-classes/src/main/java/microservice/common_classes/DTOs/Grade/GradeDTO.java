package microservice.common_classes.DTOs.Grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.Utils.Grades.GradeStatus;
import microservice.common_classes.Utils.Grades.GradeType;
import microservice.common_classes.Utils.SubjectType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GradeDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("grade_value")
    private Integer gradeValue;

    @JsonProperty("student_account_number")
    private String studentAccountNumber;

    @JsonProperty("grade_type")
    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    @JsonProperty("grade_result")
    private String gradeResult;

    @JsonProperty("school_period")
    private String schoolPeriod;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("grade_status")
    private String gradeStatus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("authorized_at")
    private LocalDateTime authorizedAt;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;
}