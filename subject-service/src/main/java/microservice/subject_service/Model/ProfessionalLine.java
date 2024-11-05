package microservice.subject_service.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "professional_line")
public class ProfessionalLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professional_line_id")
    private Long professionalLineId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "professionalLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ElectiveSubject> subjects;

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }
}
