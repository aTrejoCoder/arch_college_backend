package microservice.common_classes.DTOs.Enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.SubjectType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private String groupKey;
    private String subjectKey;
    private Long subjectId;

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;


    private String studentAccountNumber;

    private int subjectCredits;

    private LocalDateTime enrollmentDate;

    private String enrollmentPeriod;
}
