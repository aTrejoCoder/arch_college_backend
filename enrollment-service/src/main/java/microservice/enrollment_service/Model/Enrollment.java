package microservice.enrollment_service.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.SubjectType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "groups")
public class Enrollment {
    @Id
    private Long id;

    @JsonProperty("subject_key")
    private String subjectKey;

    @JsonProperty("group_key")
    private String groupKey;

    @JsonProperty("student_account_number")
    private String studentAccountNumber;

    @JsonProperty("subject_type")
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @JsonProperty("subject_credits")
    private int subjectCredits;

    @JsonProperty("enrollment_date")
    private LocalDateTime enrollmentDate;

    @JsonProperty("enrollment_period")
    private String enrollmentPeriod;

    // When Enrollment period ends all enrollments gets locked and can't be modified
    @JsonProperty("is_locked")
    private boolean isLocked;

    @PrePersist
    protected void onCreate() {
        this.enrollmentDate = LocalDateTime.now();
        this.isLocked = false;
    }

}
