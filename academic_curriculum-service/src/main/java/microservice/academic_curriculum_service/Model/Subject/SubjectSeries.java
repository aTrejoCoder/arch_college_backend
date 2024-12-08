package microservice.academic_curriculum_service.Model.Subject;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subject_series")
public class SubjectSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    @OrderBy("serialNumber ASC")
    private List<ObligatorySubject> obligatorySubjects = new ArrayList<>();

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    @OrderBy("serialNumber ASC")
    private List<ElectiveSubject> electiveSubjects = new ArrayList<>();

    public void addObligatorySubject(ObligatorySubject subject) {
        obligatorySubjects.add(subject);
        subject.setSeries(this);
    }

    public void addElectiveSubject(ElectiveSubject subject) {
        electiveSubjects.add(subject);
        subject.setSeries(this);
    }
}
