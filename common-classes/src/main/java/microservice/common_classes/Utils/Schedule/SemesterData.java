package microservice.common_classes.Utils.Schedule;


import java.time.LocalDateTime;


// Semester Format is 2024-2 --> 2024 means year - 2 means the
// number of the semester in the year (January - July = 1) (August - December = 2)
public class SemesterData {
    public static String getCurrentSemester() {

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
}
