package microservice.grade_service.Utils.Credits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreditAdvance {
    private TotalCredits totalCredits;
    private ObligatoryCredits obligatoryCredits;
    private ElectiveCredits electiveCredits;

    public void calculateAllPercentages() {
        totalCredits.calculatePercentageCompleted();
        obligatoryCredits.calculatePercentageCompleted();
        electiveCredits.calculatePercentageCompleted();
    }

    public void validateAllCredits() {
        totalCredits.validateCredits();
        obligatoryCredits.validateCredits();
        electiveCredits.validateCredits();
    }
}
