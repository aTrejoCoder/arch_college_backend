package microservice.grade_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.grade_service.Model.GradeStatus;

@Data
@NoArgsConstructor
public class GradeDTO {
    @JsonProperty("id")
    private Long id;

    @Column(name = "grade_value")
    private int gradeValue;

    @Column(name = "grade_status")
    @Enumerated(EnumType.STRING)
    private GradeStatus gradeStatus;

    @JsonProperty("ordinary_subject_id")
    private Long ordinarySubjectId;

    @JsonProperty("elective_subject_id")
    private Long electiveSubjectId;

    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("group_id")
    private Long groupId;
}
