package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("teacherId")
    private Long teacherId;

    @JsonProperty("teacher_name")
    private String teacherName;

    @JsonProperty("ordinary_subject_id")
    private Long ordinarySubjectId;

    @JsonProperty("elective_subject_id")
    private Long electiveSubjectId;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("spots")
    private int spots;

    @JsonProperty("schedule")
    private List<ScheduleDTO> schedule;

    @JsonProperty("classroom")
    private String classroom;
}
