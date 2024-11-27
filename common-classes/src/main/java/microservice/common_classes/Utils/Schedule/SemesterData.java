package microservice.common_classes.Utils.Schedule;


import java.time.LocalDateTime;


// Semester Format is 2024-2 --> 2024 means year - 2 means the
// number of the semester in the year (January - July = 1) (August - December = 2)
public class SemesterData {
    public static String getCurrentSchoolPeriod() {

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int currentMonth = now.getMonthValue();

        int semesterNumber;
        if (currentMonth < 6) {
            semesterNumber = 1;
        } else  {
            semesterNumber = 2;
        }

        return year + "-" + semesterNumber;
    }

    public static String getBehindSchoolPeriod() {
        String currentSemester = getCurrentSchoolPeriod();
        String behindSemester;

        if (currentSemester.endsWith("-2")) {
            // If current semester is the second of the year, just change it to the first semester
            behindSemester = currentSemester.replace("-2", "-1");
        } else {
            // If current semester is the first of the year, move to the second semester of the previous year
            String[] parts = currentSemester.split("-");
            int year = Integer.parseInt(parts[0]);

            behindSemester = (year - 1) + "-2";
        }
        return behindSemester;
    }
}
