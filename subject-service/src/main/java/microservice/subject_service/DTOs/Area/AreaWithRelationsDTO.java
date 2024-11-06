package microservice.subject_service.DTOs.Area;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.Model.ElectiveSubject;
import microservice.subject_service.Model.OrdinarySubject;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

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
