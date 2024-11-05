package microservice.subject_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ordinary_subject")
public class OrdinarySubject extends Subject {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_line_id", nullable = false)
    private ProfessionalLine professionalLine;

    @Column(name = "semester")
    private int semester;

    @Column(name = "credits", nullable = false)
    private int credits;
}
