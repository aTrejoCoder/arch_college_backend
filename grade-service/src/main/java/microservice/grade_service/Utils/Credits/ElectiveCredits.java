package microservice.grade_service.Utils.Credits;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ElectiveCredits extends CreditCategory {
    public ElectiveCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

        @Override
    public void validateCredits() {
        if (currentCredits > totalCredits) {
            throw new IllegalArgumentException("Elective current credits cannot exceed total credits.");
        }
    }

    @Override
    public void increaseCurrentCredits(int creditsIncome) {
        this.currentCredits += creditsIncome;
    }


}
