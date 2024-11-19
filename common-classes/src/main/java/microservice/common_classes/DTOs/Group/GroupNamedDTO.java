package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.GroupType;
import microservice.common_classes.Utils.SubjectType;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupNamedDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("teacher_name")
    private String teacherName;

    @JsonProperty("subject_type")
    private SubjectType subjectType;

    @JsonProperty("group_type")
    private GroupType groupType;
}
