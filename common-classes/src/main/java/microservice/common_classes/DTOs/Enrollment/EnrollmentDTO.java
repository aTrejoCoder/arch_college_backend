package microservice.common_classes.DTOs.Enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Group.GroupType;
import microservice.common_classes.Utils.SubjectType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EnrollmentDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("group_key")
    private String groupKey;

    @JsonProperty("subject_key")
    private String subjectKey;

    @JsonProperty("subject_period")
    private String schoolPeriod;

    @JsonProperty("student_account_number")
    private String studentAccountNumber;

}
