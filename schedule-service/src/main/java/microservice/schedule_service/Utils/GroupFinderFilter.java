package microservice.schedule_service.Utils;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class GroupFinderFilter {

    private final Map<GroupFilters, Object> filters;

    public GroupFinderFilter() {
        this.filters = new HashMap<>();
    }

    public GroupFinderFilter withSchoolPeriod(String schoolPeriod) {
        if (schoolPeriod != null) {
            filters.put(GroupFilters.SCHOOL_PERIOD, schoolPeriod);
        }
        return this;
    }

    public GroupFinderFilter withSubjectType(String subjectType) {
        if (subjectType != null) {
            filters.put(GroupFilters.SUBJECT_TYPE, subjectType);
        }
        return this;
    }

    public GroupFinderFilter withSubjectKey(String subjectKey) {
        if (subjectKey != null) {
            filters.put(GroupFilters.SUBJECT_KEY, subjectKey);
        }
        return this;
    }

    public GroupFinderFilter withClassroom(String classroom) {
        if (classroom != null) {
            filters.put(GroupFilters.CLASSROOM, classroom);
        }
        return this;
    }

    public String getFilterDetails() {
        Map<GroupFilters, Object> setFilters = getFilters();
        if (setFilters.isEmpty()) {
            return "[No filters are set]";
        }
        StringBuilder details = new StringBuilder("Active filters: [");
        setFilters.forEach((key, value) -> details.append(key).append(":").append(value).append(" "));
        details.append("]");
        return details.toString();
    }
}
