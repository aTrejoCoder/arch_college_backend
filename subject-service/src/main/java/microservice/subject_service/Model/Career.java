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
    @Column(name = "career_id")
    private Long careerId;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "title_awarded")
    private String titleAwarded;

    @Column(name = "modality")
    private String modality;

    @Column(name = "semester_duration")
    private String semesterDuration;

    @Column(name = "total_credits", nullable = false)
    private int totalCredits;

    @Column(name = "career_director_id", nullable = false)
    private Long CareerDirectorId;

    @OneToMany(mappedBy = "career", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SocialNetwork> socialNetworks;

    @OneToMany(mappedBy = "career", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdinarySubject> ordinarySubjects;

    @OneToMany(mappedBy = "career", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ElectiveSubject> electiveSubjects;
}
