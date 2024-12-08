package microservice.grade_service.Utils.Credits;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalCredits extends CreditCategory {

    @Override
    public void validateCredits() {
        if (currentCredits > totalCredits) {
            throw new IllegalArgumentException("Total current credits cannot exceed total credits.");
        }
    }

    @Override
    public void increaseCurrentCredits(int creditsIncome) {
        this.currentCredits += creditsIncome;
    }

    public TotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }
}
