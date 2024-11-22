package microservice.grade_service.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.grade_service.Utils.Credits.CreditAdvance;
import microservice.grade_service.Utils.Credits.ElectiveCredits;
import microservice.grade_service.Utils.Credits.ObligatoryCredits;
import microservice.grade_service.Utils.Credits.TotalCredits;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "academic_histories")
public class AcademicHistory {
    @Id
    @JsonProperty("student_account_number")
    private String studentAccountNumber;

    @JsonProperty("student_name")
    private String studentName;

    @JsonProperty("career_key")
    private String careerKey;

    @JsonProperty("career_name")
    private String careerName;

    @JsonProperty("speciality")
    private String speciality;

    @JsonProperty("academic_average")
    private double academicAverAge;

    @JsonProperty("credit_advance")
    private CreditAdvance creditAdvance;

    @JsonProperty("income_generation")
    private String incomeGeneration;

    @JsonProperty("grades")
    private List<GradeNamed> grades;

    public void initCreditAdvance(int totalObligatoryCredits, int totalElectiveCredits) {
        int totalCareerCredits = totalElectiveCredits + totalObligatoryCredits;

        TotalCredits totalCredits = new TotalCredits(totalCareerCredits);
        ElectiveCredits electiveCredits = new ElectiveCredits(totalElectiveCredits);
        ObligatoryCredits obligatoryCredits = new ObligatoryCredits(totalObligatoryCredits);

        this.creditAdvance = new CreditAdvance(totalCredits, obligatoryCredits, electiveCredits);
    }

    public void reCalculateAverage() {
        double newAcademicAverage = this.grades.stream()
                .mapToDouble(GradeNamed::getGradeValue)
                .average()
                .orElse(0.0);

        this.setAcademicAverAge(newAcademicAverage);
    }

    public void reCalculatePercentages() {
       this.creditAdvance.calculateAllPercentages();
    }

    public void addElectiveCreditAdvance(int creditAdvance) {
        this.creditAdvance.getElectiveCredits().increaseCurrentCredits(creditAdvance);
    }

    public void addOrdinaryCreditAdvance(int creditAdvance) {
        this.creditAdvance.getObligatoryCredits().increaseCurrentCredits(creditAdvance);
    }

    public void addTotalCreditAdvance(int creditAdvance) {
        this.creditAdvance.getTotalCredits().increaseCurrentCredits(creditAdvance);
    }
}
