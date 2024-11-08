package microservice.schedule_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.DTO.GroupDTO;
import microservice.schedule_service.DTO.GroupInsertDTO;
import microservice.schedule_service.DTO.GroupUpdateDTO;
import microservice.schedule_service.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "Get Group by ID", description = "Retrieve a group by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Group found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @ApiResponse(responseCode = "404", description = "Group not found")
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> getGroupById(
            @Parameter(description = "ID of the group to retrieve") @PathVariable Long groupId) {
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
    public ResponseEntity<ResponseWrapper<GroupDTO>> getGroupByKey(
            @Parameter(description = "Key of the group to retrieve") @PathVariable String key) {
        Result<GroupDTO> groupResult = groupService.getGroupCurrentByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }
        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Group"));
    }

    @Operation(summary = "Get Groups by Subject ID", description = "Retrieve all groups for a specific subject")
    @ApiResponse(responseCode = "200", description = "Groups retrieved", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-subject/{subjectId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsBySubjectId(
            @Parameter(description = "ID of the subject to retrieve groups for") @PathVariable Long subjectId) {
        List<GroupDTO> groupsDTOs = groupService.getCurrentGroupsBySubjectId(subjectId);
        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Subject Id", subjectId));
    }

    @Operation(summary = "Get Groups by Teacher ID", description = "Retrieve all groups for a specific teacher")
    @ApiResponse(responseCode = "200", description = "Groups retrieved", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByTeacherId(
            @Parameter(description = "ID of the teacher to retrieve groups for") @PathVariable Long teacherId) {
        List<GroupDTO> groupsDTOs = groupService.getCurrentGroupsByTeacherId(teacherId);
        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Teacher Id", teacherId));
    }

    @Operation(summary = "Create Group", description = "Create a new group with specified details")
    @ApiResponse(responseCode = "201", description = "Group created successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createGroup(
            @Valid @RequestBody GroupInsertDTO groupInsertDTO) {
        Result<Void> valdiationResult = groupService.validateGroupSchedule(groupInsertDTO.getClassroom(), groupInsertDTO.getSchedule());
        if (!valdiationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(valdiationResult.getErrorMessage()));
        }
        groupService.createGroup(groupInsertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created("Group"));
    }

    // TO BE FIXED
    @Operation(summary = "Update Group", description = "Update an existing group with specified details")
    @ApiResponse(responseCode = "200", description = "Group updated successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PutMapping
    public ResponseEntity<ResponseWrapper<Void>> updateGroup(
            @Valid @RequestBody GroupUpdateDTO groupUpdateDTO) {
        Result<Void> valdiationResult = groupService.validateGroupSchedule(groupUpdateDTO.getClassroom(), groupUpdateDTO.getSchedule());
        if (!valdiationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(valdiationResult.getErrorMessage()));
        }
        groupService.updateGroup(groupUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.updated("Group"));
    }

    @Operation(summary = "Delete Group by Key", description = "Delete an existing group by its key")
    @ApiResponse(responseCode = "200", description = "Group deleted successfully")
    @DeleteMapping("/{key}")
    public ResponseEntity<ResponseWrapper<Void>> deleteGroupByKey(
            @Parameter(description = "Key of the group to delete") @PathVariable String key) {
        groupService.deleteCurrentGroupByKey(key);
        return ResponseEntity.ok(ResponseWrapper.deleted("Group"));
    }
}
