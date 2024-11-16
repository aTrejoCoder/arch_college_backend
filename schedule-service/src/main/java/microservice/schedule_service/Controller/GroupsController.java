package microservice.schedule_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Models.GroupRelationshipsDTO;
import microservice.schedule_service.Service.ExternalRelationsService;
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
    private final ExternalRelationsService externalDataService;

    @Autowired
    public GroupsController(GroupService groupService,
                            ScheduleService scheduleService,
                            ExternalRelationsService externalDataService) {
        this.groupService = groupService;
        this.scheduleService = scheduleService;
        this.externalDataService = externalDataService;
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

    @Operation(summary = "Get Group by Key", description = "Retrieve a group by its unique key")
    @ApiResponse(responseCode = "200", description = "Group found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @ApiResponse(responseCode = "404", description = "Group not found")
    @GetMapping("/by-classroom/{classroom}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupByClassroom(@Parameter(description = "Key of the group to retrieve") @PathVariable String classroom) {
        List<GroupDTO> groupList = groupService.getCurrentGroupsByClassroom(classroom);
        return ResponseEntity.ok(ResponseWrapper.found(groupList, "Group"));
    }

    @GetMapping("/by-building/{classroom}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByBuilding(@PathVariable char buildingLetter) {
        List<GroupDTO> groupList = groupService.getCurrentGroupsByClassroomPrefix(buildingLetter);
        return ResponseEntity.ok(ResponseWrapper.found(groupList, "Group"));
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
    @PostMapping("/ordinary")
    public ResponseEntity<ResponseWrapper<GroupDTO>> createOrdinaryGroup(@Valid @RequestBody OrdinaryGroupInsertDTO ordinaryGroupInsertDTO) {
        Result<GroupRelationshipsDTO> relationshipsResult = externalDataService.validateAndGetRelationships(ordinaryGroupInsertDTO);
        if (!relationshipsResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(relationshipsResult.getErrorMessage()));
        }

        // Creation: Group ID = Null || Update: Group ID = Not Null
        CompletableFuture<Result<Void>> classroomScheduleResultFuture = scheduleService.validateClassroomSchedule(ordinaryGroupInsertDTO.getClassroom(), ordinaryGroupInsertDTO.getSchedule(), null);
        CompletableFuture<Result<Void>> teacherScheduleResultFuture = scheduleService.validateTeachersSchedule(ordinaryGroupInsertDTO.getTeacherIds(), ordinaryGroupInsertDTO.getSchedule(), null);

        CompletableFuture.allOf(classroomScheduleResultFuture, teacherScheduleResultFuture);
        Result<Void> teacherScheduleResult = classroomScheduleResultFuture.join();
        Result<Void> teacherResult = teacherScheduleResultFuture.join();

        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        if (!teacherScheduleResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherScheduleResult.getErrorMessage()));
        }

        GroupDTO group = groupService.createGroup(ordinaryGroupInsertDTO, relationshipsResult.getData());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(group,"Group"));
    }


    @Operation(summary = "Create Group", description = "Create a new group with specified details")
    @ApiResponse(responseCode = "201", description = "Group created successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PostMapping("/elective")
    public ResponseEntity<ResponseWrapper<GroupDTO>> createElectiveGroup(@Valid @RequestBody ElectiveGroupInsertDTO electiveGroupInsertDTO) {
        Result<GroupRelationshipsDTO> relationshipsResult = externalDataService.validateAndGetRelationships(electiveGroupInsertDTO);
        if (!relationshipsResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(relationshipsResult.getErrorMessage()));
        }

        // Creation: Group ID = Null || Update: Group ID = Not Null
        CompletableFuture<Result<Void>> classroomScheduleResultFuture = scheduleService.validateClassroomSchedule(electiveGroupInsertDTO.getClassroom(), electiveGroupInsertDTO.getSchedule(), null);
        CompletableFuture<Result<Void>> teacherScheduleResultFuture = scheduleService.validateTeacherSchedule(electiveGroupInsertDTO.getTeacherId(), electiveGroupInsertDTO.getSchedule(), null);

        CompletableFuture.allOf(classroomScheduleResultFuture, teacherScheduleResultFuture);
        Result<Void> teacherScheduleResult = classroomScheduleResultFuture.join();
        Result<Void> teacherResult = teacherScheduleResultFuture.join();

        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        if (!teacherScheduleResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherScheduleResult.getErrorMessage()));
        }

        GroupDTO group = groupService.createGroup(electiveGroupInsertDTO, relationshipsResult.getData());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(group,"Group"));
    }

    @Operation(summary = "Update Schedule Group", description = "Update an existing group with specified details")
    @ApiResponse(responseCode = "200", description = "Group updated successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PatchMapping("/update-schedule")
    public ResponseEntity<ResponseWrapper<GroupDTO>> updateGroupSchedule(@Valid @RequestBody GroupScheduleUpdateDTO groupScheduleUpdateDTO) {
        Result<Void> teacherResult = scheduleService.validateClassroomSchedule(groupScheduleUpdateDTO.getClassroom(), groupScheduleUpdateDTO.getSchedule(), groupScheduleUpdateDTO.getGroup_id()).join();
        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        GroupDTO groupDTO = groupService.updateGroupSchedule(groupScheduleUpdateDTO);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.updated(groupDTO,"Group"));
    }

    @PatchMapping("/{key}/add_spots/{spotsToAdd}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> increaseGroupSpotsByKey(@Valid @PathVariable String key, @PathVariable int spotsToAdd) {
        Result<GroupDTO> groupResult = groupService.getGroupCurrentByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }

        Result<Void> validationResult = groupService.validateSpotIncrease(spotsToAdd);
        if (!validationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(validationResult.getErrorMessage()));
        }

        GroupDTO groupDTO = groupService.addSpots(key, spotsToAdd);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.ok(groupDTO,"Spots Successfully increased"));
    }

    @PatchMapping("/{key}/remove-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> removeGroupTeacher(@Valid @PathVariable String key, @PathVariable Long teacherId) {
        Result<GroupDTO> groupResult = groupService.getGroupCurrentByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }

        GroupDTO groupDTO = groupService.removeTeacher(key, teacherId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.ok(groupDTO,"Spots Successfully increased"));
    }

    // TODO: Create Update Group Teacher

    @Operation(summary = "Delete Group by Key", description = "Delete an existing group by its key")
    @ApiResponse(responseCode = "200", description = "Group deleted successfully")
    @DeleteMapping("/{key}")
    public ResponseEntity<ResponseWrapper<Void>> deleteGroupByKey(@Parameter(description = "Key of the group to delete") @PathVariable String key) {
        groupService.deleteCurrentGroupByKey(key);
        return ResponseEntity.ok(ResponseWrapper.deleted("Group"));
    }
}
