package microservice.common_classes.Utils.Student;

import lombok.Getter;
import java.util.Set;

    @Getter
    public enum StudentFilter {
        SEMESTERS_COMPLETED("semestersCompleted"),
        LAST_NAME("lastName"),
        FIRST_NAME("firstName"),
        CAREER("careerId"),
        PROFESSIONAL_LINE("professionalLineId"),
        INCOME_GENERATION("incomeGeneration");

        private final String entityField;

        StudentFilter(String entityField) {
            this.entityField = entityField;
        }

        public static boolean isValid(String filter) {
            try {
                valueOf(filter.toUpperCase());
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