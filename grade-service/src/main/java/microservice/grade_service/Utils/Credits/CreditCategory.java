package microservice.grade_service.Utils.Credits;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class CreditCategory {
    @JsonProperty("current_credits")
    protected int currentCredits;

    @JsonProperty("total_credits")
    protected int totalCredits;

    @JsonProperty("percentage_completed")
    protected Double percentageCompleted;

    public void calculatePercentageCompleted() {
        if (totalCredits > 0) {
            this.percentageCompleted = (currentCredits * 100.0) / totalCredits;
        } else {
            this.percentageCompleted = 0.0;
        }
    }

    public abstract void validateCredits();

    public abstract void increaseCurrentCredits(int creditIncome);
}
