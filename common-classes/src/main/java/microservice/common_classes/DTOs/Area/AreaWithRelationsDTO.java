package microservice.common_classes.DTOs.Area;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AreaWithRelationsDTO {
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private Page<OrdinarySubjectDTO> ordinarySubjects;

    private Page<ElectiveSubjectDTO> electiveSubjects;


    public void setRelationships(Page<OrdinarySubjectDTO> ordinarySubjects,
                                 Page<ElectiveSubjectDTO> electiveSubjects) {
        this.ordinarySubjects = ordinarySubjects;
        this.electiveSubjects = electiveSubjects;
    }
}