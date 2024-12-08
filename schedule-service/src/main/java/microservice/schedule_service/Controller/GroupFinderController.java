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
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.schedule_service.Service.GroupServices.GroupFinderService;
import microservice.schedule_service.Utils.GroupFinderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Groups API", description = "Endpoints for finding groups by various parameters and filters")
@RestController
@RequestMapping("/v1/api/finder/groups")
@RequiredArgsConstructor
public class GroupFinderController {

    private final GroupFinderService groupFinderService;
    private final String currentSchoolPeriod = AcademicData.getCurrentSchoolPeriod();

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
    public ResponseEntity<ResponseWrapper<GroupDTO>> getCurrentGroupByKey(
            @Parameter(description = "Key of the group to retrieve") @PathVariable String key) {
        Result<GroupDTO> groupResult = groupFinderService.getCurrentGroupByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }
        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Group (School Period:" + AcademicData.getCurrentSchoolPeriod() + ")"));
    }

    @Operation(summary = "Get Groups by IDs", description = "Retrieve multiple groups by their unique IDs")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = GroupDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid IDs provided")
    @GetMapping("/by-ids/{idList}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByIds(
            @Parameter(description = "List of group IDs to retrieve") @Valid @PathVariable List<Long> idList) {
        Result<List<GroupDTO>> groupResult = groupFinderService.getGroupsByIds(idList);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.ok(ResponseWrapper.badRequest(groupResult.getErrorMessage()));
        }
        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Groups"));
    }

    @Operation(summary = "Find Groups with Dynamic Filters", description = "Retrieve groups based on various dynamic filters and pagination")
    @ApiResponse(responseCode = "200", description = "Groups successfully fetched", content = @Content(schema = @Schema(implementation = Page.class)))
    @ApiResponse(responseCode = "400", description = "Invalid filter parameters provided")
    @GetMapping("/by")
    public ResponseEntity<ResponseWrapper<Page<GroupDTO>>> getGroups(
            @Parameter(description = "School period to filter groups by (e.g., '2023-1' '2023-2','2024-1')") @RequestParam(required = false) String schoolPeriod,
            @Parameter(description = "Type of the subject (e.g., 'Obligatory' or 'Elective')") @RequestParam(required = false) String subjectType,
            @Parameter(description = "Key of the subject (e.g., 'CS101')") @RequestParam(required = false) String subjectKey,
            @Parameter(description = "Classroom (nomenclature e.g 'A-101', 'C-203') to filter groups by") @RequestParam(required = false) String classroom,
            @Parameter(description = "Page number for pagination") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Size of the page for pagination") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Field to sort by") @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @Parameter(description = "Sorting order (asc or desc)") @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        GroupFinderFilter groupFinderFilter = new GroupFinderFilter()
                .withSchoolPeriod(schoolPeriod)
                .withSubjectType(subjectType)
                .withSubjectKey(subjectKey)
                .withClassroom(classroom);

        Page<GroupDTO> groups = groupFinderService.findGroupsWithFilters(groupFinderFilter, pageable);

        return ResponseEntity.ok(ResponseWrapper.ok(groups, "Groups Successfully Fetched. Filters Applied:"
                + groupFinderFilter.getFilterDetails() + " --> Sort Details:" + pageable));
    }

    @Operation(summary = "Get Current Groups", description = "Retrieve all groups for the current school period with pagination")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/current")
    public ResponseEntity<ResponseWrapper<Page<GroupDTO>>> getCurrentGroups(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupDTO> groupPage = groupFinderService.getCurrentGroups(pageable);
        return ResponseEntity.ok(ResponseWrapper.found(groupPage, "Groups (School Period:" + currentSchoolPeriod + ")"));
    }

    @Operation(summary = "Get Current Groups by Teacher ID", description = "Retrieve all groups for the current school period based on teacher ID")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = List.class)))
    @ApiResponse(responseCode = "404", description = "Groups not found for the given teacher ID")
    @GetMapping("/current/by-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getCurrentGroupsByTeacherId(
            @Parameter(description = "Teacher ID to filter groups by") @PathVariable Long teacherId) {
        List<GroupDTO> groupList = groupFinderService.getCurrentGroupByTeacherId(teacherId);
        return ResponseEntity.ok(ResponseWrapper.found(groupList, "Groups (School Period:" + currentSchoolPeriod + ")"));
    }

    @Operation(summary = "Get Current Groups by Building", description = "Retrieve groups based on a classroom prefix or building letter for the current school period")
    @ApiResponse(responseCode = "200", description = "Groups found", content = @Content(schema = @Schema(implementation = List.class)))
    @GetMapping("/current/by-building/{buildingLetter}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getCurrentGroupsByBuilding(
            @Parameter(description = "Building letter to filter groups by") @PathVariable char buildingLetter) {
        List<GroupDTO> groupList = groupFinderService.getCurrentGroupsByClassroomPrefix(String.valueOf(buildingLetter));
        return ResponseEntity.ok(ResponseWrapper.found(groupList, "Groups (School Period:" + currentSchoolPeriod + ")"));
    }
}