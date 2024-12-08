package microservice.grade_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Group.GroupType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", unique = true)
    private Long groupId;

    @Column(name = "head_teacher_account_number")
    private String headTeacherAccountNumber;

    @Column(name = "is_group_qualified")
    private boolean isGroupQualified;

    @Column(name = "qualified_at")
    private LocalDateTime qualifiedAt;

    @Column(name = "group_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Grade> grades;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public Set<String> getStudentAccountNumbers() {
        if (this.grades.isEmpty()) {
            throw  new RuntimeException("This group don't have grades");
        }

        return this.grades.stream()
                .map(Grade::getStudentAccountNumber)
                .collect(Collectors.toSet());
    }
}
