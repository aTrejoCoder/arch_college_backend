package microservice.common_classes.DTOs.Grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.Utils.Grades.GradeStatus;

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

    @JsonProperty("professional_line_id")
    private Long professionalLineId;

    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("student")
    private StudentDTO studentDTO;

    @JsonProperty("group")
    private GroupDTO groupDTO;

    @JsonProperty("elective_subject")
    private ElectiveSubjectDTO electiveSubjectDTO;

    @JsonProperty("ordinary_subject")
    private OrdinarySubjectDTO ordinarySubjectDTO;
}