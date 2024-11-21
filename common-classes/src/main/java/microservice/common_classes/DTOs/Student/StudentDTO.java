package microservice.common_classes.DTOs.Student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.ProfessionalLineModality;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StudentDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("professional_line_id")
    private Long professionalLineId;

    @JsonProperty("professional_line_modality")
    private ProfessionalLineModality professionalLineModality;

    @JsonProperty("date_of_birth")
    private LocalDateTime dateOfBirth;

    @JsonProperty("semesters_completed")
    private int semestersCompleted;

    @JsonProperty("career_id")
    private Long careerId;

    @JsonProperty("income_generation")
    private String incomeGeneration;
}