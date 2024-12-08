package microservice.common_classes.Utils.Schedule;

import java.time.LocalDateTime;

public class AcademicData {

    private static final int SEMESTER_ONE_END_MONTH = 6;
    private static final int SEMESTER_TWO_START_MONTH = 7;
    private static final int SEMESTER_START_MONTH_ONE = 1;
    private static final int SEMESTER_START_MONTH_TWO = 7;

    // Semester Format is 2024-2 --> 2024 means year, 2 is the semester number
    public static String getCurrentSchoolPeriod() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int currentMonth = now.getMonthValue();

        int semesterNumber = (currentMonth <= SEMESTER_ONE_END_MONTH) ? 1 : 2;
        return year + "-" + semesterNumber;
    }

    public static String getBehindSchoolPeriod() {
        String currentSemester = getCurrentSchoolPeriod();

        if (currentSemester.endsWith("-2")) {
            return currentSemester.replace("-2", "-1");
        } else {
            String[] parts = currentSemester.split("-");
            int year = Integer.parseInt(parts[0]);
            return (year - 1) + "-2";
        }
    }

    public static LocalDateTime getCurrentSchoolPeriodStartDate() {
        String currentSchoolPeriod = getCurrentSchoolPeriod();
        String[] parts = currentSchoolPeriod.split("-");

        int year = Integer.parseInt(parts[0]);
        int semesterStartMonth = (parts[1].equals("2")) ? SEMESTER_TWO_START_MONTH : SEMESTER_START_MONTH_ONE;

        return LocalDateTime.of(year, semesterStartMonth, 1, 0, 0, 0);
    }

    public static LocalDateTime getGradingStartPeriodTime() {
        if (isSemesterStartMonth()) {
            return LocalDateTime.now().minusMonths(1);
        } else {
            return getCurrentSchoolPeriodStartDate().plusMonths(5);
        }
    }

    public static LocalDateTime getGradingEndPeriodTime() {
        if (isSemesterStartMonth()) {
            int endMonth = LocalDateTime.now().getMonthValue() + 1;
            int year = LocalDateTime.now().getYear();
            return LocalDateTime.of(year, endMonth, 1, 0, 0, 0);
        } else {
            return getCurrentSchoolPeriodStartDate().plusMonths(2);
        }
    }

    public static boolean isSemesterStartMonth() {
        int currentMonth = LocalDateTime.now().getMonthValue();
        return currentMonth == SEMESTER_START_MONTH_ONE || currentMonth == SEMESTER_START_MONTH_TWO;
    }
}
