package microservice.schedule_service.Service.GroupServices;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.schedule_service.Utils.GroupFinderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupFinderService {
    Result<GroupDTO> getGroupById(Long groupId);
    Result<List<GroupDTO>> getGroupsByIds(List<Long> groupsId);
    Result<GroupDTO> getCurrentGroupByKey(String key);
    Page<GroupDTO> findGroupsWithFilters(GroupFinderFilter groupFinderFilter, Pageable pageable);
    List<GroupDTO> getCurrentGroupByTeacherId(Long teacherId);
    Page<GroupDTO> getCurrentGroups(Pageable pageable);
    List<GroupDTO> getCurrentGroupsByClassroomPrefix(String buildingLetter);
}
