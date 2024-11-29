package microservice.enrollment_service.Model.Preload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Grades.GradeStatus;
import microservice.common_classes.Utils.SubjectType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "grade")
public class Grade {
    @Id
    private Long id;

    @JsonProperty("grade_value")
    private int gradeValue;

    @JsonProperty("grade_status")
    private String gradeStatus;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("subject_type")
    private String subjectType;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("student_account_number")
    private String studentAccountNumber;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("school_period")
    private String schoolPeriod;

    @JsonProperty("is_authorized")
    private boolean isAuthorized;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("authorized_at")
    private LocalDateTime authorizedAt;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;
}


