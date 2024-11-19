package microservice.grade_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.grade_service.Utils.GradeStatus;

@Data
@NoArgsConstructor
public class GradeWithRelationsDTO {
    @JsonProperty("id")
    private Long id;

    @Column(name = "grade_value")
    private int gradeValue;

    @Column(name = "grade_status")
    @Enumerated(EnumType.STRING)
    private GradeStatus gradeStatus;

    @JsonProperty("student")
    private StudentDTO studentDTO;

    @JsonProperty("group")
    private GroupDTO groupDTO;

    @JsonProperty("elective_subject")
    private ElectiveSubjectDTO electiveSubjectDTO;

    @JsonProperty("ordinary_subject")
    private OrdinarySubjectDTO ordinarySubjectDTO;
}
