package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Teacher.TeacherNameDTO;
import microservice.common_classes.Utils.Group.GroupStatus;
import microservice.common_classes.Utils.Group.GroupType;
import microservice.common_classes.Utils.SubjectType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("group_key")
    private String groupKey;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("subject_key")
    private String subjectKey;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("subject_type")
    private SubjectType subjectType;

    @JsonProperty("available_spots")
    private int availableSpots;

    @JsonProperty("status")
    private GroupStatus groupStatus;

    @JsonProperty("group_type")
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @JsonProperty("schedule")
    private List<ScheduleDTO> schedule;

    @JsonProperty("classroom")
    private String classroom;

    private List<TeacherNameDTO> teacherDTOS = new ArrayList<>();
}
