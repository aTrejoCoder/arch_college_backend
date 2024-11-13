package microservice.schedule_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.GroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupRelationshipsDTO;
import microservice.common_classes.DTOs.Group.GroupUpdateDTO;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Service.ExternalDataValidationService;
import microservice.schedule_service.Service.GroupService;
import microservice.schedule_service.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// TODO: Implement Named DTOs and Fix Update Group

@Tag(name = "Groups API", description = "Endpoints for managing groups")
@RestController
@RequestMapping("/v1/api/groups")
public class GroupsController {

    private final GroupService groupService;
    private final ScheduleService scheduleService;
    private final ExternalDataValidationService externalDataValidationService;

    @Autowired
    public GroupsController(GroupService groupService, ScheduleService scheduleService,
                            ExternalDataValidationService externalDataValidationService) {
        this.groupService = groupService;
        this.scheduleService = scheduleService;
        this.externalDataValidationService = externalDataValidationService;
    }

    @Operation(summary = "Get Group by ID", description = "Retrieve a group by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Group found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @ApiResponse(responseCode = "404", description = "Group not found")
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> getGroupById(@Parameter(description = "ID of the group to retrieve") @PathVariable Long groupId) {
        Result<GroupDTO> groupResult = groupService.getGroupById(groupId);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }
        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Group"));
    }

    @Operation(summary = "Get Group by Key", description = "Retrieve a group by its unique key")
    @ApiResponse(responseCode = "200", description = "Group found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @ApiResponse(responseCode = "404", description = "Group not found")
    @GetMapping("/key/{key}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> getGroupByKey(@Parameter(description = "Key of the group to retrieve") @PathVariable String key) {
        Result<GroupDTO> groupResult = groupService.getGroupCurrentByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }
        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Group"));
    }

    @Operation(summary = "Get Groups by Subject ID", description = "Retrieve all groups for a specific subject")
    @ApiResponse(responseCode = "200", description = "Groups retrieved", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-subject/{subjectId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsBySubjectId(@Parameter(description = "ID of the subject to retrieve groups for") @PathVariable Long subjectId) {
        List<GroupDTO> groupsDTOs = groupService.getCurrentGroupsBySubjectId(subjectId);
        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Subject Id", subjectId));
    }

    @Operation(summary = "Get Groups by Teacher ID", description = "Retrieve all groups for a specific teacher")
    @ApiResponse(responseCode = "200", description = "Groups retrieved", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByTeacherId(@Parameter(description = "ID of the teacher to retrieve groups for") @PathVariable Long teacherId) {
        List<GroupDTO> groupsDTOs = groupService.getCurrentGroupsByTeacherId(teacherId);
        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Teacher Id", teacherId));
    }

    @Operation(summary = "Create Group", description = "Create a new group with specified details")
    @ApiResponse(responseCode = "201", description = "Group created successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PostMapping
    public ResponseEntity<ResponseWrapper<GroupDTO>> createGroup(@Valid @RequestBody GroupInsertDTO groupInsertDTO) {
        Result<GroupRelationshipsDTO> relationshipsResult = externalDataValidationService.validateAndGetRelationships(groupInsertDTO);
        if (!relationshipsResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(relationshipsResult.getErrorMessage()));
        }

        // Creation: Group ID = Null || Update: Group ID = Not Null
        CompletableFuture<Result<Void>> classroomScheduleResultFuture = scheduleService.validateClassroomSchedule(groupInsertDTO.getClassroom(), groupInsertDTO.getSchedule(), null);
        CompletableFuture<Result<Void>> teacherScheduleResultFuture = scheduleService.validateTeacherSchedule(groupInsertDTO.getTeacherId(), groupInsertDTO.getSchedule(), null);

        CompletableFuture.allOf(classroomScheduleResultFuture, teacherScheduleResultFuture);
        Result<Void> teacherScheduleResult = classroomScheduleResultFuture.join();
        Result<Void> teacherResult = teacherScheduleResultFuture.join();

        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        if (!teacherScheduleResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        GroupDTO group = groupService.createGroup(groupInsertDTO, relationshipsResult.getData());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(group,"Group"));
    }

    // Fix Schedule Conflict
    @Operation(summary = "Update Group", description = "Update an existing group with specified details")
    @ApiResponse(responseCode = "200", description = "Group updated successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PutMapping
    public ResponseEntity<ResponseWrapper<Void>> updateGroup(@Valid @RequestBody GroupUpdateDTO groupUpdateDTO) {
        // Creation: Group ID = Null || Update: Group ID = Not Null
        CompletableFuture<Result<Void>> classroomScheduleResultFuture = scheduleService.validateClassroomSchedule(groupUpdateDTO.getClassroom(), groupUpdateDTO.getSchedule(), groupUpdateDTO.getGroup_id());
        CompletableFuture<Result<Void>> teacherScheduleResultFuture = scheduleService.validateTeacherSchedule(groupUpdateDTO.getTeacherId(), groupUpdateDTO.getSchedule(), groupUpdateDTO.getGroup_id());

        CompletableFuture.allOf(classroomScheduleResultFuture, teacherScheduleResultFuture);
        Result<Void> teacherScheduleResult = classroomScheduleResultFuture.join();
        Result<Void> teacherResult = teacherScheduleResultFuture.join();

        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        if (!teacherScheduleResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.updated("Group"));
    }

    @Operation(summary = "Delete Group by Key", description = "Delete an existing group by its key")
    @ApiResponse(responseCode = "200", description = "Group deleted successfully")
    @DeleteMapping("/{key}")
    public ResponseEntity<ResponseWrapper<Void>> deleteGroupByKey(@Parameter(description = "Key of the group to delete") @PathVariable String key) {
        groupService.deleteCurrentGroupByKey(key);
        return ResponseEntity.ok(ResponseWrapper.deleted("Group"));
    }
}
