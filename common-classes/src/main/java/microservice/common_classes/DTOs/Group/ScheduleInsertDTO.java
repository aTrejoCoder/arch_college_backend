package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Schedule.WEEKDAY;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class ScheduleInsertDTO {
        @Enumerated(EnumType.STRING)
        @NotNull(message = "day can't be null")
        private WEEKDAY day;

        @JsonProperty("start_time")
        @NotNull(message = "start_time can't be null")
        private LocalTime startTime;

        @JsonProperty("end_time")
        @NotNull(message = "end_time can't be null")
        private LocalTime endTime;
}
