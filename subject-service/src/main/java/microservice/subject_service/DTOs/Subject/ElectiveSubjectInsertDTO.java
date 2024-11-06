package microservice.subject_service.DTOs.Subject;

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
public class ElectiveSubjectInsertDTO {
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be empty")
    private String name;

    @JsonProperty("area_id")
    @NotNull(message = "area_id can't be null")
    @Positive(message = "area_id can't be negative")
    private Long areaId;

    @JsonProperty("career_id")
    @NotNull(message = "career_id can't be null")
    @Positive(message = "career_id can't be negative")
    private Long careerId;


    @JsonProperty("professional_line_id")
    @NotNull(message = "professional_line_id can't be null")
    @Positive(message = "professional_line_id can't be negative")
    private Long professionalLineId;

    @JsonProperty("subject_program_url")
    private String subjectProgramUrl;
}
