package microservice.student_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import microservice.common_classes.Models.Person;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
public class Student extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "current_credits")
    private int currentCredits;

    @Column(name = "semesters_completed")
    private int semestersCompleted;

    @Column(name = "income_generation")
    private String incomeGeneration;

    public void initializeAcademicValues(String accountNumber, String incomeGeneration) {
        this.accountNumber = accountNumber;
        this.incomeGeneration = incomeGeneration;
        this.currentCredits = 0;
        this.semestersCompleted = 0;
    }
}
