package microservice.schedule_service.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Schedule.TimeRange;
import microservice.common_classes.Utils.Schedule.WEEKDAY;


import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "day", nullable = false)
    @Enumerated(EnumType.STRING)
    private WEEKDAY day;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    public TimeRange getTimeRange() {
        return new TimeRange(startTime, endTime);
    }

    public void setTimeRange(TimeRange timeRange) {
        this.startTime = timeRange.start();
        this.endTime = timeRange.end();
    }

    public Schedule(WEEKDAY day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
