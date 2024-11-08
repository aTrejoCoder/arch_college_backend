package microservice.schedule_service.Models;

import lombok.Getter;

import java.time.LocalTime;
import java.time.Duration;

public record TimeRange(LocalTime start, LocalTime end) {
    public TimeRange {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    public boolean overlaps(TimeRange other) {
        return !this.end.isBefore(other.start) && !other.end.isBefore(this.start);
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }

    @Override
    public String toString() {
        return "TimeRange{" + "start=" + start + ", end=" + end + '}';
    }

}