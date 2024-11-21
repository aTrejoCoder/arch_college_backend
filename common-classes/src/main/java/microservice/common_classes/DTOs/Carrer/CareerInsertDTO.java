package microservice.common_classes.DTOs.Carrer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerInsertDTO {
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be empty")
    private String name;

    @JsonProperty("title_awarded")
    @NotNull(message = "title_awarded can't be null")
    @NotBlank(message = "title_awarded can't be empty")
    private String titleAwarded;

    @JsonProperty("modality")
    @NotNull(message = "modality can't be null")
    @NotBlank(message = "modality can't be empty")
    private String modality;

    @JsonProperty("semester_duration")
    @NotNull(message = "semester_duration can't be null")
    @NotBlank(message = "semester_duration can't be empty")
    private String semesterDuration;

    @JsonProperty("total_career_credits")
    @NotNull(message = "total_career_credits can't be null")
    @Positive(message = "total_career_credits can't be negative")
    private int totalCareerCredits;

    @JsonProperty("total_obligatory_credits")
    @NotNull(message = "total_obligatory_credits can't be null")
    @Positive(message = "total_obligatory_credits can't be negative")
    private int totalObligatoryCredits;

    @JsonProperty("total_elective_credits")
    @NotNull(message = "total_elective_credits can't be null")
    @Positive(message = "total_elective_credits can't be negative")
    private int totalElectiveCredits;


    @JsonProperty("career_director_id")
    @NotNull(message = "total_credits can't be null")
    @Positive(message = "total_credits can't be negative")
    private Long careerDirectorId;
}