package microservice.subject_service.DTOs.Career;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.subject_service.Model.ElectiveSubject;
import microservice.subject_service.Model.OrdinarySubject;
import microservice.subject_service.Model.SocialNetwork;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;

    @JsonProperty("title_awarded")
    private String titleAwarded;

    @JsonProperty("modality")
    private String modality;

    @JsonProperty("semester_duration")
    private String semesterDuration;

    @JsonProperty("total_credits")
    private int totalCredits;

    @JsonProperty("career_director_id")
    private Long careerDirectorId;
}
