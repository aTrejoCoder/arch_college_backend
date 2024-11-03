package microservice.teacher_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.teacher_service.Model.Title;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TeacherInsertDTO {

    @NotNull(message = "title is obligatory")
    @JsonProperty("title")
    @Enumerated(EnumType.STRING)
    private Title title;

    @NotNull(message = "first_name is obligatory")
    @NotBlank(message = "first_name can't be blank")
    @Size(min = 3, message = "first_name must have at least 3 characters")
    @JsonProperty("first_name")
    private String firstName;

    @NotNull(message = "last_name is obligatory")
    @NotBlank(message = "last_name can't be blank")
    @Size(min = 3, message = "last_name must have at least 3 characters")
    @JsonProperty("last_name")
    private String lastName;

    @Pattern(regexp = "^[A-Z]{4}\\d{6}[HM][A-Z]{5}[A-Z0-9]\\d$", message = "CURP format is invalid")
    @Size(min = 18, max = 18, message = "CURP must be 18 characters long")
    @JsonProperty("curp")
    private String curp;

    @JsonProperty("rfc")
    @Pattern(regexp = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$", message = "RFC must have the correct format of 13 characters")
    private String rfc;

    @JsonProperty("nss")
    @Pattern(regexp = "^\\d{11}$", message = "Social security number must be 11 digits")
    private String nss;

    @JsonProperty("date_of_birth")
    @NotNull(message = "date_of_birth is obligatory")
    @Past(message = "date_of_birth must be a past date")
    private LocalDateTime dateOfBirth;

    @NotNull(message = "speciality is obligatory")
    @NotBlank(message = "speciality can't be blank")
    @Size(min = 3, message = "speciality must have at least 3 characters")
    @JsonProperty("speciality")
    private String speciality;

    @JsonProperty("hired_at")
    @NotNull(message = "hired_at is obligatory")
    private LocalDateTime hired_at;

    @JsonProperty("salary_per_month")
    @NotNull(message = "salary_per_month is obligatory")
    @Positive(message = "salary_per_month must be a positive number")
    private double salaryPerMonth;
}
