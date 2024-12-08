package microservice.grade_service.Utils;

import lombok.Getter;

@Getter
public enum GradeValues {
        ZERO(0),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        NO_PRESENT(-1);

        private final int numericValue;

        GradeValues(int numericValue) {
                this.numericValue = numericValue;
        }
}
