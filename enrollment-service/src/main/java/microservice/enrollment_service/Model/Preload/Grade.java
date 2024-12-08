package microservice.enrollment_service.Model.Preload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Grades.GradeStatus;
import microservice.common_classes.Utils.SubjectType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "grades")
public class Grade {
    @Id
    private Long id;

    @JsonProperty("grade_value")
    @Field("grade_value")
    private int gradeValue;

    @JsonProperty("grade_status")
    @Field("grade_status")
    private GradeStatus gradeStatus;

    @JsonProperty("subject_id")
    @Field("subject_id")
    private Long subjectId;

    @JsonProperty("subject_type")
    @Field("subject_type")
    private SubjectType subjectType;

    @JsonProperty("subject_name")
    @Field("subject_name")
    private String subjectName;

    @JsonProperty("student_account_number")
    @Field("student_account_number")
    private String studentAccountNumber;

    @JsonProperty("group_id")
    @Field("group_id")
    private Long groupId;

    @JsonProperty("professional_line_id")
    @Field("professional_line_id")
    private Long professionalLineId;

    @JsonProperty("school_period")
    @Field("school_period")
    private String schoolPeriod;

    @JsonProperty("is_authorized")
    @Field("is_authorized")
    private boolean isAuthorized;

    @JsonProperty("created_at")
    @Field("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("authorized_at")
    @Field("authorized_at")
    private LocalDateTime authorizedAt;

    @JsonProperty("deleted_at")
    @Field("deleted_at")
    private LocalDateTime deletedAt;

    public boolean isApproved() {
        return gradeValue >= 6
                && this.gradeStatus != GradeStatus.NOT_ACCREDITED;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", gradeValue=" + gradeValue +
                ", gradeStatus='" + gradeStatus + '\'' +
                ", subjectId=" + subjectId +
                ", subjectType=" + subjectType +
                ", subjectName='" + subjectName + '\'' +
                ", studentAccountNumber='" + studentAccountNumber + '\'' +
                ", groupId=" + groupId +
                ", professionalLineId=" + professionalLineId +
                ", schoolPeriod='" + schoolPeriod + '\'' +
                ", isAuthorized=" + isAuthorized +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", authorizedAt=" + authorizedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }

}


