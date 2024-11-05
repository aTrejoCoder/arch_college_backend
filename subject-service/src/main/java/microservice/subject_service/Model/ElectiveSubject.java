package microservice.subject_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "elective_subject")
public class ElectiveSubject extends Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "elective_subject_id")
    private Long electiveSubjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_line_id", nullable = false)
    private ProfessionalLine professionalLine;
}
