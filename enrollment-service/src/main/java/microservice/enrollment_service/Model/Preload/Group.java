package microservice.enrollment_service.Model.Preload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import microservice.common_classes.DTOs.Group.ScheduleDTO;
import microservice.common_classes.DTOs.Teacher.TeacherNameDTO;
import microservice.common_classes.Utils.Group.GroupStatus;
import microservice.common_classes.Utils.Group.GroupType;
import microservice.common_classes.Utils.SubjectType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "groups")
public class Group {
    @JsonProperty("id")
    @Id
    private Long id;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("subject_key")
    private String subjectKey;

    @JsonProperty("subject_type")
    private SubjectType subjectType;

    @JsonProperty("key")
    private String key;

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

    @JsonProperty("teachers")
    private List<TeacherNameDTO> teacherDTOS;
}
