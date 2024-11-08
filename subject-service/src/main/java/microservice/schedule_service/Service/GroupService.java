package microservice.schedule_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.schedule_service.DTO.GroupDTO;
import microservice.schedule_service.DTO.GroupInsertDTO;
import microservice.schedule_service.DTO.GroupUpdateDTO;
import microservice.schedule_service.DTO.ScheduleInsertDTO;
import microservice.schedule_service.Models.Schedule;
import microservice.schedule_service.Models.WEEKDAY;

import java.util.List;

public interface GroupService {
    Result<GroupDTO> getGroupById(Long groupId);
    Result<GroupDTO> getGroupByKey(String key);
    List<GroupDTO> getGroupsBySubjectId(Long subjectId);
    List<GroupDTO> getGroupsByTeacherId(Long subjectId);
    void createGroup(GroupInsertDTO groupInsertDTO);
    void updateGroup(GroupUpdateDTO groupUpdateDTO);
    void deleteGroupByKey(String key);
    Result<Void> validateGroupSchedule(String classroom, List<ScheduleInsertDTO> schedules);
}
