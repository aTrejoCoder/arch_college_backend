package microservice.common_classes.DTOs.Enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Group.GroupType;
import microservice.common_classes.Utils.SubjectType;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentGradeDTO {
    @JsonProperty("enrollment_id")
    private Long enrollmentId;

    @JsonProperty("student_account_number")
    private String studentAccountNumber;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_type")
    private GroupType groupType;

    @JsonProperty("head_teacher_account_number")
    private String headTeacherAccountNumber;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("subject_type")
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("subject_credits")
    private int subjectCredits;

    @JsonProperty("school_period")
    private String schoolPeriod;
}
