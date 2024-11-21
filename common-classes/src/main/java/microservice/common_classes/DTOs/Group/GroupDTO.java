package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Teacher.TeacherNameDTO;
import microservice.common_classes.Utils.Group.GroupStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("obligatory_subject_id")
    private Long obligatorySubjectId;

    @JsonProperty("elective_subject_id")
    private Long electiveSubjectId;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("key")
    private String key;

    @JsonProperty("available_spots")
    private int availableSpots;

    @JsonProperty("status")
    private GroupStatus groupStatus;

    @JsonProperty("schedule")
    private List<ScheduleDTO> schedule;

    @JsonProperty("classroom")
    private String classroom;

    private List<TeacherNameDTO> teacherDTOS = new ArrayList<>();
}
