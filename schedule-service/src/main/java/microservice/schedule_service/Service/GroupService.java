package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.Utils.Result;
import java.util.List;

public interface GroupService {
    Result<GroupDTO> getGroupById(Long groupId);
    Result<GroupDTO> getGroupCurrentByKey(String key);
    List<GroupDTO> getCurrentGroupsBySubjectId(Long subjectId);
    List<GroupDTO> getCurrentGroupsByTeacherId(Long subjectId);

    GroupDTO createGroup(GroupInsertDTO groupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO);
    GroupDTO updateGroup(GroupUpdateDTO groupUpdateDTO, GroupRelationshipsDTO groupRelationshipsDTO);
    void deleteCurrentGroupByKey(String key);


}