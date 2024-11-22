package microservice.common_classes.DTOs.Student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StudentExtendedDTO {
    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("professional_line")
    private ProfessionalLineDTO professionalLineDTO;

    @JsonProperty("career")
    private CareerDTO careerDTO;

    @JsonProperty("professional_line_modality")
    private ProfessionalLineModality professionalLineModality;

    @JsonProperty("date_of_birth")
    private LocalDateTime dateOfBirth;

    @JsonProperty("current_credits")
    private int currentCredits;

    @JsonProperty("semesters_completed")
    private int semestersCompleted;

    @JsonProperty("income_generation")
    private String incomeGeneration;
}