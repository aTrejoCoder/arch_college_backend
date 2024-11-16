package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.Utils.GroupStatus;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Models.GroupRelationshipsDTO;

import java.util.List;

public interface GroupService {
    Result<GroupDTO> getGroupById(Long groupId);
    Result<GroupDTO> getGroupCurrentByKey(String key);
    List<GroupDTO> getCurrentGroupsByClassroom(String classroom);
    List<GroupDTO> getCurrentGroupsByClassroomPrefix(char buildingLetter);


    List<GroupDTO> getCurrentGroupsBySubjectId(Long subjectId);
    List<GroupDTO> getCurrentGroupsByTeacherId(Long subjectId);

    GroupDTO createGroup(OrdinaryGroupInsertDTO ordinaryGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO);
    GroupDTO createGroup(ElectiveGroupInsertDTO ordinaryGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO);

    GroupDTO updateGroupSchedule(GroupScheduleUpdateDTO groupUpdateDTO);
    GroupDTO updateGroupTeacher(GroupScheduleUpdateDTO groupUpdateDTO);
    GroupDTO updateGroupStatus(GroupStatus groupStatus, String key);
    GroupDTO addSpots(String key, int spotsToAdd);
    GroupDTO removeTeacher(String groupKey, Long teacherId);
    Result<Void> validateSpotIncrease(int spotsToAdd);

    void deleteCurrentGroupByKey(String key);
}
