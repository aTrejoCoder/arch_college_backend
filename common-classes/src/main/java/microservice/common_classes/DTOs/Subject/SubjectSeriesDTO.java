package microservice.common_classes.DTOs.Subject;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectSeriesDTO {
    private Long id;

    private String name;

    private List<ObligatorySubjectDTO> obligatorySubjects = new ArrayList<>();
    private List<ElectiveSubjectDTO> electiveSubjects = new ArrayList<>();
}
