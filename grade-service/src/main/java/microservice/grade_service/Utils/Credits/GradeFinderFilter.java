package microservice.grade_service.Utils.Credits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.SubjectType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeFinderFilter {
    private String accountNumber;
    private String schoolPeriod;
    private Long subjectId;
    private SubjectType subjectType;

    public String getActiveFilters() {
        List<String> activeFilters = new ArrayList<>();
        if (accountNumber != null) {
            activeFilters.add("accountNumber = " + accountNumber);
        }
        if (schoolPeriod != null) {
            activeFilters.add("schoolPeriod = " + schoolPeriod);
        }
        if (subjectId != null) {
            activeFilters.add("subjectId = " + subjectId);
        }
        if (subjectType != null) {
            activeFilters.add("subjectType = " + subjectType);
        }

        if (activeFilters.isEmpty()) {
            return "No filters set. All grades will be fetched.";
        }

        return String.join(", ", activeFilters);
    }
}
