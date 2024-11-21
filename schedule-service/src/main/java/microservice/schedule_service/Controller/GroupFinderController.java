package microservice.schedule_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Service.GroupServices.GroupFinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Groups API", description = "Endpoints for finding groups by various parameters and filters")
@RestController
@RequestMapping("/v1/api/groups")
@RequiredArgsConstructor
public class GroupFinderController {

    private final GroupFinderService groupFinderService;

    @Operation(summary = "Get Group by ID", description = "Retrieve a group by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Group found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @ApiResponse(responseCode = "404", description = "Group not found")
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> getGroupById(
            @Parameter(description = "ID of the group to retrieve") @PathVariable Long groupId) {
        Result<GroupDTO> groupResult = groupFinderService.getGroupById(groupId);
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
        Result<GroupDTO> groupResult = groupFinderService.getGroupCurrentByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }
        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Group"));
    }

    @Operation(summary = "Get Groups by Classroom", description = "Retrieve a list of groups by classroom for the current school period")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @ApiResponse(responseCode = "404", description = "Groups not found")
    @GetMapping("/by-classroom/{classroom}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupByClassroom(
            @Parameter(description = "Classroom identifier") @PathVariable String classroom) {
        List<GroupDTO> groupList = groupFinderService.getCurrentGroupsByClassroom(classroom);
        return ResponseEntity.ok(ResponseWrapper.found(groupList, "Group"));
    }

    @Operation(summary = "Get Groups by IDs", description = "Retrieve multiple groups by their unique IDs")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid IDs provided")
    @GetMapping("/by-ids/{idList}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsById(
            @Parameter(description = "List of group IDs to retrieve") @Valid @PathVariable List<Long> idList) {
        Result<List<GroupDTO>> groupResult = groupFinderService.getGroupsByIds(idList);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.ok(ResponseWrapper.badRequest(groupResult.getErrorMessage()));
        }
        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Groups"));
    }

    @Operation(summary = "Get Groups by Building", description = "Retrieve groups based on a classroom prefix or building letter")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-building/{buildingLetter}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByBuilding(
            @Parameter(description = "Building letter to filter groups by") @PathVariable char buildingLetter) {
        List<GroupDTO> groupList = groupFinderService.getCurrentGroupsByClassroomPrefix(buildingLetter);
        return ResponseEntity.ok(ResponseWrapper.found(groupList, "Groups"));
    }

    @Operation(summary = "Get Groups by Obligatory Subject ID", description = "Retrieve all groups associated with an obligatory subject")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-obligatory-subject/{subjectId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByObligatorySubjectId(
            @Parameter(description = "ID of the obligatory subject") @PathVariable Long subjectId) {
        List<GroupDTO> groupsDTOs = groupFinderService.getCurrentGroupsByObligatorySubjectId(subjectId);
        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Subject Id", subjectId));
    }

    @Operation(summary = "Get Groups by Elective Subject ID", description = "Retrieve all groups associated with an elective subject")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-elective-subject/{subjectId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByElectiveSubjectId(
            @Parameter(description = "ID of the elective subject") @PathVariable Long subjectId) {
        List<GroupDTO> groupsDTOs = groupFinderService.getCurrentGroupsByElectiveSubjectId(subjectId);
        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Subject Id", subjectId));
    }

    @Operation(summary = "Get Groups by Teacher ID", description = "Retrieve all groups for a specific teacher")
    @ApiResponse(responseCode = "200", description = "Groups retrieved", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByTeacherId(
            @Parameter(description = "ID of the teacher to retrieve groups for") @PathVariable Long teacherId) {
        List<GroupDTO> groupsDTOs = groupFinderService.getCurrentGroupsByTeacherId(teacherId);
        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Teacher Id", teacherId));
    }
}
