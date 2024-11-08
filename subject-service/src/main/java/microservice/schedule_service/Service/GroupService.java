package microservice.schedule_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.schedule_service.DTO.*;

import java.util.List;

public interface GroupService {
    Result<GroupDTO> getGroupById(Long groupId);
    Result<GroupDTO> getGroupCurrentByKey(String key);
    List<GroupDTO> getCurrentGroupsBySubjectId(Long subjectId);
    List<GroupDTO> getCurrentGroupsByTeacherId(Long subjectId);

    void createGroup(GroupInsertDTO groupInsertDTO);
    void updateGroup(GroupUpdateDTO groupUpdateDTO);
    void deleteCurrentGroupByKey(String key);
    Result<Void> validateGroupSchedule(String classroom, List<ScheduleInsertDTO> schedules);
}
