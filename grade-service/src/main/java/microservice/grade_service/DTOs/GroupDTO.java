package microservice.grade_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Group.GroupType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDTO {
    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("head_teacher_account_number")
    private String headTeacherAccountNumber;

    @JsonProperty("group_type")
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    private String subjectName;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<GradeDTO> grades;


}
