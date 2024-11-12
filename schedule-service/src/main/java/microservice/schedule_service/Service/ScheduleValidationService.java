package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Group.ScheduleInsertDTO;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleValidationService {

    private final GroupRepository groupRepository;

    @Autowired
    public ScheduleValidationService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Result<Void> validateSchedule(String classroom, List<ScheduleInsertDTO> schedules, String schoolperiod) {
        List<Group> existingGroups = groupRepository.findByClassroomAndSchoolPeriod(classroom, schoolperiod);

        for (ScheduleInsertDTO newSchedule : schedules) {
            for (Group group : existingGroups) {
                for (var existingSchedule : group.getSchedule()) {
                    if (existingSchedule.getDay().equals(newSchedule.getDay()) &&
                            isTimeRangeOverlapping(newSchedule.getStartTime(), newSchedule.getEndTime(),
                                    existingSchedule.getTimeRange().start(), existingSchedule.getTimeRange().end())) {
                        return Result.error("Schedule conflict detected with another group in the same classroom.");
                    }
                }
            }
        }
        return Result.success();
    }

    private boolean isTimeRangeOverlapping(LocalTime newStart, LocalTime newEnd, LocalTime existingStart, LocalTime existingEnd) {
        return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart));
    }
}
