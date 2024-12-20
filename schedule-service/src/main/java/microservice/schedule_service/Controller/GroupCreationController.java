package microservice.schedule_service.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.ElectiveGroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.ObligatoryGroupInsertDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.schedule_service.Models.GroupRelationshipsDTO;
import microservice.schedule_service.Service.GroupServices.Implementation.GroupCreationService;
import microservice.schedule_service.Service.GroupServices.Implementation.GroupRelationshipService;
import microservice.schedule_service.Service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Tag(name = "Groups API", description = "Endpoints for managing groups")
@RestController
@RequestMapping("/v1/api/groups")
@RequiredArgsConstructor
public class GroupCreationController {

    private final GroupCreationService groupCreationService;
    private final GroupRelationshipService groupRelationshipService;
    private final ScheduleService scheduleService;

    @Operation(summary = "Create Group", description = "Create a new group with specified details")
    @ApiResponse(responseCode = "201", description = "Group created successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/obligatory")
    public ResponseEntity<ResponseWrapper<GroupDTO>> createObligatoryGroup(@Valid @RequestBody ObligatoryGroupInsertDTO OBligatoryGroupInsertDTO) {
        Result<GroupRelationshipsDTO> relationshipsResult = groupRelationshipService.validateAndGetRelationships(OBligatoryGroupInsertDTO);
        if (!relationshipsResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(relationshipsResult.getErrorMessage()));
        }

        System.out.println(relationshipsResult.getData());

        // Creation: Group ID = Null || Update: Group ID = Not Null
        CompletableFuture<Result<Void>> classroomScheduleResultFuture = scheduleService.validateClassroomSchedule(OBligatoryGroupInsertDTO.getClassroom(), OBligatoryGroupInsertDTO.getSchedule(), null);
        CompletableFuture<Result<Void>> teacherScheduleResultFuture = scheduleService.validateTeachersSchedule(OBligatoryGroupInsertDTO.getTeacherIds(), OBligatoryGroupInsertDTO.getSchedule(), null);

        CompletableFuture.allOf(classroomScheduleResultFuture, teacherScheduleResultFuture);
        Result<Void> teacherScheduleResult = classroomScheduleResultFuture.join();
        Result<Void> teacherResult = teacherScheduleResultFuture.join();

        if (!teacherResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherResult.getErrorMessage()));
        }

        if (!teacherScheduleResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(teacherScheduleResult.getErrorMessage()));
        }

        GroupDTO group = groupCreationService.createGroup(OBligatoryGroupInsertDTO, relationshipsResult.getData());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(group,"Group"));
    }

    @Operation(summary = "Create Group", description = "Create a new group with specified details")
    @ApiResponse(responseCode = "201", description = "Group created successfully")
    @ApiResponse(responseCode = "409", description = "Conflict in group schedule")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/elective")
    public ResponseEntity<ResponseWrapper<GroupDTO>> createElectiveGroup(@Valid @RequestBody ElectiveGroupInsertDTO electiveGroupInsertDTO) {
        Result<GroupRelationshipsDTO> relationshipsResult = groupRelationshipService.validateAndGetRelationships(electiveGroupInsertDTO);
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

        GroupDTO group = groupCreationService.createGroup(electiveGroupInsertDTO, relationshipsResult.getData());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(group,"Group"));
    }
}
