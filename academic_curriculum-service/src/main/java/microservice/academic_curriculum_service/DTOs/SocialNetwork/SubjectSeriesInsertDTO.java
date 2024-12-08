package microservice.academic_curriculum_service.DTOs.SocialNetwork;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.SubjectType;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectSeriesInsertDTO {
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    private String name;

    @NotNull(message = "subjectsIds can't be null")
    private Set<Long> subjectsIds;

    @NotNull(message = "subject_type can't be null")
    private SubjectType subjectType;

}
