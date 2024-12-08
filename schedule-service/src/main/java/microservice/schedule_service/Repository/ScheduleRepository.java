package microservice.schedule_service.Repository;

import microservice.common_classes.Utils.Schedule.WEEKDAY;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
        List<Schedule> findByDayAndStartTimeAndEndTime(WEEKDAY day, LocalTime startTime, LocalTime endTime);

}
