package microservice.enrollment_service.Model.Preload;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "subject_serialization")
public class SubjectSeries {
    @Id
    private Long id;

    private String name;

    private List<ObligatorySubject> obligatorySubjects = new ArrayList<>();
    private List<ElectiveSubject> electiveSubjects = new ArrayList<>();
}
