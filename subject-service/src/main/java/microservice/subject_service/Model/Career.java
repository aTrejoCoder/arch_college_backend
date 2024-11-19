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
@Table(name = "career")
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "title_awarded")
    private String titleAwarded;

    @Column(name = "modality")
    private String modality;

    @Column(name = "semester_duration")
    private String semesterDuration;

    @Column(name = "total_career_credits", nullable = false)
    private int totalCareerCredits;

    @Column(name = "total_obligatory_credits", nullable = false)
    private int totalObligatoryCredits;

    @Column(name = "total_elective_credits", nullable = false)
    private int totalElectiveCredits;

    @Column(name = "career_director_id", nullable = false)
    private Long careerDirectorId;

    @OneToMany(mappedBy = "career", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ObligatorySubject> obligatorySubjects;

    @OneToMany(mappedBy = "career", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ElectiveSubject> electiveSubjects;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
