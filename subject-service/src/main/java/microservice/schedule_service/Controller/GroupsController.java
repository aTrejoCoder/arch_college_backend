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

import javax.security.auth.Subject;
import java.util.List;

@RestController
@RequestMapping("/v1/api/groups")
public class GroupsController {

    private final GroupService groupService;

    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> getGroupById(@PathVariable Long groupId) {
        Result<GroupDTO> groupResult = groupService.getGroupById(groupId);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Group"));
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<ResponseWrapper<GroupDTO>> getGroupByKey(@PathVariable String key) {
        Result<GroupDTO> groupResult = groupService.getGroupByKey(key);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Group"));
    }

    @GetMapping("/by-subject/{subjectId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsBySubjectId(@PathVariable Long subjectId) {
        List<GroupDTO> groupsDTOs = groupService.getGroupsBySubjectId(subjectId);

        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Subject Id", subjectId));
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<ResponseWrapper<List<GroupDTO>>> getGroupsByTeacherId(@PathVariable Long teacherId) {
        List<GroupDTO> groupsDTOs = groupService.getGroupsByTeacherId(teacherId);

        return ResponseEntity.ok(ResponseWrapper.found(groupsDTOs, "Groups", "Subject Id", teacherId));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createGroup(@Valid @RequestBody GroupInsertDTO groupInsertDTO) {
        Result<Void> valdiationResult = groupService.validateGroupSchedule(groupInsertDTO.getClassroom(), groupInsertDTO.getSchedule());
        if (!valdiationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(valdiationResult.getErrorMessage()));
        }

        groupService.createGroup(groupInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created("Group"));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper<Void>> updateGroup(@Valid @RequestBody GroupUpdateDTO groupUpdateDTO) {
        Result<Void> valdiationResult = groupService.validateGroupSchedule(groupUpdateDTO.getClassroom(), groupUpdateDTO.getSchedule());
        if (!valdiationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseWrapper.conflict(valdiationResult.getErrorMessage()));
        }

        groupService.updateGroup(groupUpdateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.updated("Group"));
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<ResponseWrapper<Void>> deleteGroupByKey(@PathVariable String key) {
        groupService.deleteGroupByKey(key);

        return ResponseEntity.ok(ResponseWrapper.deleted( "Group"));
    }
}
