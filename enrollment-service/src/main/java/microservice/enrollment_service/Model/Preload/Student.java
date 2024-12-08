package microservice.enrollment_service.Model.Preload;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import microservice.common_classes.Models.Person;
import microservice.common_classes.Utils.ProfessionalLineModality;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "students")
public class Student {
    @Id
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    private LocalDateTime dateOfBirth;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("curp")
    private String curp;

    @JsonProperty("semesters_completed")
    private int semestersCompleted;

    @JsonProperty("income_generation")
    private String incomeGeneration;

    @JsonProperty("career_id")
    private Long careerId;

    @JsonProperty("professional_line_id")
    private Long professionalLineId;

    @JsonProperty("professional_line_modality")
    private ProfessionalLineModality professionalLineModality;

}
