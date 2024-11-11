package microservice.common_classes.DTOs.Student;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StudentInsertDTO {
    @JsonProperty("first_name")
    @NotNull(message = "first_name is obligatory")
    @NotEmpty(message = "first_name can't be empty")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "last_name is obligatory")
    @NotEmpty(message = "last_name can't be empty")
    private String lastName;

    @JsonProperty("date_of_birth")
    @NotNull(message = "date_of_birth is obligatory")
    @Past(message = "date_of_birth must be on a past date")
    private LocalDateTime dateOfBirth;
}