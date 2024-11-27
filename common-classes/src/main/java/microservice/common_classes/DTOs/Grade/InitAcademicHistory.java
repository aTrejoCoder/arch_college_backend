package microservice.common_classes.DTOs.Grade;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitAcademicHistory {
    @NotNull
    @Valid
    private StudentDTO student;

    @NotNull
    @Valid
    private CareerDTO career;

}