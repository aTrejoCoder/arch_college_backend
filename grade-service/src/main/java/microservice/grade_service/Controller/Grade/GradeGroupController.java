package microservice.grade_service.Controller.Grade;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.DTOs.GroupDTO;
import microservice.grade_service.Model.Group;
import microservice.grade_service.Service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/grades/groups")
@RequiredArgsConstructor
public class GradeGroupController {

    private final GroupService groupService;

    @RequestMapping("/{groupId}")
    private ResponseEntity<ResponseWrapper<GroupDTO>> getGradeGroupById(@PathVariable Long groupId) {
        Result<GroupDTO> groupResult = groupService.getGroupById(groupId);
        if (!groupResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound(groupResult.getErrorMessage()));
        }

        return ResponseEntity.ok(ResponseWrapper.found(groupResult.getData(), "Grade Group"));
    }

    @RequestMapping("/pending")
    private ResponseEntity<ResponseWrapper<Page<GroupDTO>>> getPendingGradeGroups(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupDTO> groups = groupService.getPendingGroups(pageable);

        return ResponseEntity.ok(ResponseWrapper.found(groups, "Grade Group"));
    }
}
