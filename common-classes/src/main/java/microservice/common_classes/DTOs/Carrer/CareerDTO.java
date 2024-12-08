package microservice.common_classes.DTOs.Carrer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @JsonProperty("total_career_credits")
    private int totalCareerCredits;

    @JsonProperty("total_obligatory_credits")
    private int totalObligatoryCredits;

    @JsonProperty("total_elective_credits")
    private int totalElectiveCredits;

    @JsonProperty("career_director_id")
    private Long careerDirectorId;
}
