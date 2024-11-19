package microservice.subject_service.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "obligatory_subject")
public class ObligatorySubject extends Subject {
    @Column(name = "number")
    private int number;

    @Column(name = "semester")
    private int semester;

    @Column(name = "credits", nullable = false)
    private int credits;
}
