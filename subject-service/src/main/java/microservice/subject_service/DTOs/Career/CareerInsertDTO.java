package microservice.subject_service.DTOs.Career;

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

    @JsonProperty("total_credits")
    @NotNull(message = "total_credits can't be null")
    @Positive(message = "total_credits can't be negative")
    private int totalCredits;

    @JsonProperty("career_director_id")
    @NotNull(message = "total_credits can't be null")
    @Positive(message = "total_credits can't be negative")
    private Long CareerDirectorId;
}
