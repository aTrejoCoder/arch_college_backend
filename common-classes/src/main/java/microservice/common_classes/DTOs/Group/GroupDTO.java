package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.GroupStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {
    @JsonProperty("id")
    private Long id;

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

    @JsonProperty("status")
    private GroupStatus groupStatus;

    @JsonProperty("schedule")
    private List<ScheduleDTO> schedule;

    @JsonProperty("classroom")
    private String classroom;
}
