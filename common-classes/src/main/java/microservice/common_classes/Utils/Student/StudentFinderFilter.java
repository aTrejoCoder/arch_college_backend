package microservice.common_classes.Utils.Student;

import java.util.Set;

public enum StudentFinderFilter {
        CAREER_ID,
        PROFESSIONAL_LINE_ID,
        LAST_NAME,
        FIRST_NAME,
        INCOME_GENERATION,
        SEMESTERS_COMPLETED;


    public static boolean isValid(String value) {
        if (value == null) return false;
        try {
            StudentFilter.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String[] getFinderFilters() {
        return Set.of(StudentFilter.values())
                .stream()
                .map(Enum::toString)
                .toArray(String[]::new);
    }
}