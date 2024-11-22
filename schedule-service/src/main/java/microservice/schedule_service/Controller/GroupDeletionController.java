package microservice.schedule_service.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.schedule_service.Service.GroupServices.GroupDeleteServiceImpl;
import microservice.schedule_service.Service.GroupServices.GroupFinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Groups API", description = "Endpoints for managing groups")
@RestController
@RequestMapping("/v1/api/groups")
@RequiredArgsConstructor
public class GroupDeletionController {

    private final GroupDeleteServiceImpl groupDeleteService;
    private final GroupFinderService groupFinderService;

    @Operation(
            summary = "Remove Teacher from Group",
            description = "Remove a teacher from a specific group and update the group's available spots",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Teacher successfully removed, and spots updated"),
                    @ApiResponse(responseCode = "404", description = "Group or teacher not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    @PatchMapping("/{key}/remove-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> deleteTeacherInGroup(
            @Parameter(description = "Key of the group to update") @Valid @PathVariable String key,
            @Parameter(description = "ID of the teacher to remove") @PathVariable Long teacherId) {
        GroupDTO groupDTO = groupDeleteService.deleteTeacher(key, teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.ok(groupDTO, "Spots successfully increased"));
    }

    @Operation(
            summary = "Delete Group by Key",
            description = "Delete a specific group using its unique key",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Group successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Group not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    @DeleteMapping("/{key}")
    public ResponseEntity<ResponseWrapper<Void>> deleteGroupByKey(
            @Parameter(description = "Key of the group to delete") @PathVariable String key) {
        groupDeleteService.deleteCurrentGroupByKey(key);
        return ResponseEntity.ok(ResponseWrapper.deleted("Group"));
    }
}
