package microservice.schedule_service.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Service.GroupServices.GroupDeleteServiceImpl;
import microservice.schedule_service.Service.GroupServices.GroupFinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// Update Add Teacher to Group --> Cancel Group

@Tag(name = "Groups API", description = "Endpoints for managing groups")
@RestController
@RequestMapping("/v1/api/groups")
@RequiredArgsConstructor
public class GroupDeletionController {

    private final GroupDeleteServiceImpl groupDeleteService;
    private final GroupFinderService groupFinderService;

    @PatchMapping("/{key}/remove-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> deleteTeacherInGroup(@Valid @PathVariable String key, @PathVariable Long teacherId) {
        GroupDTO groupDTO = groupDeleteService.deleteTeacher(key, teacherId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.ok(groupDTO,"Spots Successfully increased"));
    }


    @Operation(summary = "Delete Group by Key", description = "Delete an existing group by its key")
    @ApiResponse(responseCode = "200", description = "Group deleted successfully")
    @DeleteMapping("/{key}")
    public ResponseEntity<ResponseWrapper<Void>> deleteGroupByKey(@Parameter(description = "Key of the group to delete") @PathVariable String key) {
        groupDeleteService.deleteCurrentGroupByKey(key);

        return ResponseEntity.ok(ResponseWrapper.deleted("Group"));
    }
}
