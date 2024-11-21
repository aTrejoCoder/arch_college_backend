package microservice.schedule_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.GroupScheduleUpdateDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Service.GroupServices.GroupFinderService;
import microservice.schedule_service.Service.GroupServices.GroupSpotsService;
import microservice.schedule_service.Service.GroupServices.GroupUpdateServiceImpl;
import microservice.schedule_service.Service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "Groups API", description = "Endpoints for managing groups")
@RestController
@RequestMapping("/v1/api/groups")
public class GroupUpdateController {

    private final ScheduleService scheduleService;
    private final GroupUpdateServiceImpl groupUpdateService;
    private final GroupSpotsService groupSpotsService;
    private final GroupFinderService groupFinderService;

    @Operation(summary = "Update Schedule Group", description = "Update an existing group with specified details")
    @ApiResponse(responseCode = "200", description = "Group updated successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PatchMapping("/update-schedule")
    public ResponseEntity<ResponseWrapper<GroupDTO>> updateGroupSchedule(@Valid @RequestBody GroupScheduleUpdateDTO groupScheduleUpdateDTO) {
        Result<Void> teacherResult = scheduleService.validateClassroomSchedule(groupScheduleUpdateDTO.getClassroom(), groupScheduleUpdateDTO.getSchedule(), groupScheduleUpdateDTO.getGroup_id()).join();
        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        GroupDTO groupDTO = groupUpdateService.updateGroupSchedule(groupScheduleUpdateDTO);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.updated(groupDTO,"Group"));
    }


    @PatchMapping("/{key}/add-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> addTeacherToGroup(@Valid @PathVariable String key,
                                                                       @PathVariable Long teacherId) {
        Result<GroupDTO> groupResult = groupFinderService.getGroupCurrentByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }

        GroupDTO group = groupResult.getData();

        Result<Void> teacherResult = scheduleService.validateTeacherSchedule(teacherId, group.getSchedule(), group.getId()).join();
        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        Result<GroupDTO> addResult = groupUpdateService.addTeacherToGroup(key, teacherId);
        if (!addResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(addResult.getErrorMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.ok(addResult.getData(),"Teacher Successfully Added to Group"));
    }

    @PatchMapping("/{key}/add_spots/{spotsToAdd}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> increaseGroupSpotsByKey(@Valid @PathVariable String key, @PathVariable int spotsToAdd) {
        Result<GroupDTO> groupResult = groupFinderService.getGroupCurrentByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }

        Result<Void> validationResult = groupSpotsService.validateSpotIncrease(spotsToAdd);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(validationResult.getErrorMessage()));
        }

        GroupDTO groupDTO = groupSpotsService.addSpots(key, spotsToAdd);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.ok(groupDTO,"Spots Successfully increased"));
    }


    @PatchMapping("/{groupKey}/decrease-spot")
    public ResponseEntity<ResponseWrapper<Void>> decreaseGroupSpotsByKey(@Valid @PathVariable String groupKey) {
        Result<Void> validationResult = groupSpotsService.decreaseSpot(groupKey);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.badRequest(validationResult.getErrorMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.ok("Spot Successfully decrease"));
    }

    @PatchMapping("/{groupKey}/increase-spot")
    public ResponseEntity<ResponseWrapper<Void>> increaseGroupSpotsByKey(@Valid @PathVariable String groupKey) {
        groupSpotsService.increaseSpot(groupKey);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.ok("Spot Successfully increased"));
    }

}