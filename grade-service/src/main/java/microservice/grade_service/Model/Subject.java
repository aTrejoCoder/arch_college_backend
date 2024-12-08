package microservice.grade_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.SubjectType;

import java.util.List;

@Entity
@Table(name = "subjects")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "subject_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "subject_credits")
    private int subjectCredits;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Grade> grades;
}
