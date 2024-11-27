package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Group.ScheduleDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.Schedule;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
@Service
public class ScheduleService {

    private final GroupRepository groupRepository;
    private final ScheduleRepository scheduleRepository;
    private final String currentSemester = SemesterData.getCurrentSchoolPeriod();

    @Autowired
    public ScheduleService(GroupRepository groupRepository, ScheduleRepository scheduleRepository) {
        this.groupRepository = groupRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Async("taskExecutor")
    public CompletableFuture<Result<Void>> validateClassroomSchedule(String classroom, List<ScheduleDTO> schedules, Long groupId) {
        if (classroom == null || schedules == null) {
            return CompletableFuture.completedFuture(Result.success());
        }

        return CompletableFuture.supplyAsync(() -> {
            List<Group> existingGroups = groupRepository.findByClassroomAndSchoolPeriod(classroom, currentSemester)
                    .stream()
                    .filter(group -> groupId == null || !groupId.equals(group.getId()))
                    .toList();

            for (ScheduleDTO newSchedule : schedules) {
                if (hasScheduleConflict(newSchedule, existingGroups)) {
                    return Result.error("Schedule conflict detected: The requested schedule is already assigned to another group at the same time.");
                }
            }

            return Result.success();
        });
    }

    @Async("taskExecutor")
    public CompletableFuture<Result<Void>> validateTeacherSchedule(Long teacherId, List<ScheduleDTO> schedules, Long groupId) {
        if (teacherId == null || schedules == null) {
            return CompletableFuture.completedFuture(Result.success());
        }

        return CompletableFuture.supplyAsync(() -> {
            List<Group> existingGroups = groupRepository.findByTeacherIdAndSchoolPeriod(teacherId, currentSemester)
                    .stream()
                    .filter(group -> groupId == null || !groupId.equals(group.getId()))
                    .toList();

            for (ScheduleDTO newSchedule : schedules) {
                if (hasScheduleConflict(newSchedule, existingGroups)) {
                    return Result.error("Schedule conflict detected: The teacher with ID " + teacherId + " already assigned to another group at the same time.");
                }
            }

            return Result.success();
        });
    }

    @Async("taskExecutor")
    public CompletableFuture<Result<Void>> validateTeachersSchedule(Set<Long> teacherIds, List<ScheduleDTO> schedules, Long groupId) {
        if (teacherIds.isEmpty() || schedules == null) {
            return CompletableFuture.completedFuture(Result.success());
        }

        return CompletableFuture.allOf(
                teacherIds.stream()
                        .map(teacherId -> validateTeacherSchedule(teacherId, schedules, groupId))
                        .toArray(CompletableFuture[]::new)
        ).thenApply(v -> {
            for (Long teacherId : teacherIds) {
                Result<Void> result = validateTeacherScheduleResult(teacherId, schedules, groupId);
                if (!result.isSuccess()) {
                    return Result.error(result.getErrorMessage());
                }
            }
            return Result.success();
        });
    }

    private Result<Void> validateTeacherScheduleResult(Long teacherId, List<ScheduleDTO> schedules, Long groupId) {
        CompletableFuture<Result<Void>> future = validateTeacherSchedule(teacherId, schedules, groupId);
        return future.join();
    }

    private boolean hasScheduleConflict(ScheduleDTO newSchedule, List<Group> existingGroups) {
        for (Group group : existingGroups) {
            if (isConflictWithGroupSchedule(newSchedule, group.getSchedule())) {
                return true;
            }
        }
        return false;
    }

    private boolean isConflictWithGroupSchedule(ScheduleDTO newSchedule, List<Schedule> existingSchedules) {
        for (Schedule existingSchedule : existingSchedules) {
            if (isSameDay(newSchedule, existingSchedule) && isTimeOverlapping(newSchedule, existingSchedule)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameDay(ScheduleDTO newSchedule, Schedule existingSchedule) {
        return existingSchedule.getDay().equals(newSchedule.getDay());
    }

    private boolean isTimeOverlapping(ScheduleDTO newSchedule, Schedule existingSchedule) {
        return isTimeRangeOverlapping(
                newSchedule.getStartTime(),
                newSchedule.getEndTime(),
                existingSchedule.getTimeRange().start(),
                existingSchedule.getTimeRange().end()
        );
    }

    private boolean isTimeRangeOverlapping(LocalTime newStart, LocalTime newEnd, LocalTime existingStart, LocalTime existingEnd) {
        return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart));
    }

    public List<Schedule> mapScheduleDTOToEntity(List<ScheduleDTO> scheduleDTOs) {
        List<Schedule> scheduleList = new ArrayList<>();

        for (ScheduleDTO scheduleDTO : scheduleDTOs) {
            List<Schedule> schedules = scheduleRepository.findByDayAndStartTimeAndEndTime(scheduleDTO.getDay(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime());

            if (!schedules.isEmpty()) {
                scheduleList.add(schedules.get(0));
            } else {
                Schedule newSchedule = new Schedule(scheduleDTO.getDay(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime());
                scheduleRepository.saveAndFlush(newSchedule);
                scheduleList.add(newSchedule);
            }
        }

        return scheduleList;
    }
}