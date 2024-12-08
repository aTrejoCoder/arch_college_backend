package microservice.grade_service.Utils.Credits;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObligatoryCredits extends CreditCategory {
    public ObligatoryCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    @Override
    public void validateCredits() {
        if (currentCredits > totalCredits) {
            throw new IllegalArgumentException("Obligatory current credits cannot exceed total credits.");
        }
    }

    @Override
    public void increaseCurrentCredits(int creditsIncome) {
        this.currentCredits += creditsIncome;
    }
}
