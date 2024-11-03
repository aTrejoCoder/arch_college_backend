package microservice.teacher_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.teacher_service.Model.Title;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TeacherDTO {
    @JsonProperty("teacher_id")
    private Long teacherId;

    @JsonProperty("title")
    private Title Title;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("speciality")
    private String speciality;

    @JsonProperty("hired_at")
    private LocalDateTime hired_at;

    @JsonProperty("salary_per_month")
    private double salaryPerMonth;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    private LocalDateTime dateOfBirth;

    @JsonProperty("curp")
    private String curp;

    @JsonProperty("rfc")
    private String rfc;

    @JsonProperty("nss")
    private String nss;
}
