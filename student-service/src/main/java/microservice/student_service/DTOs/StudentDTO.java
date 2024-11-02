package microservice.student_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Date;

@Data
@NoArgsConstructor
public class StudentDTO {
    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    private LocalDateTime dateOfBirth;

    @JsonProperty("current_credits")
    private int currentCredits;

    @JsonProperty("semesters_completed")
    private int semestersCompleted;

    @JsonProperty("income_generation")
    private String incomeGeneration;
}
