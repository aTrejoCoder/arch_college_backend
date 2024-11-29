package microservice.schedule_service.Service.GroupServices;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.common_classes.Utils.SubjectType;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Utils.GroupMappingService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupFinderService {

    private final GroupRepository groupRepository;
    private final GroupQueryService queryService;
    private final GroupMappingService groupMappingService;
    private final String CURRENT_SEMESTER = SemesterData.getCurrentSchoolPeriod();


    public Group findGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found for ID: " + id));
    }

    @Cacheable(value = "groupById", key = "#groupId")
    public Result<GroupDTO> getGroupById(Long groupId) {
        return queryService.findGroupById(groupId)
                .map(groupMappingService::mapGroupToDTOWithTeachers)
                .map(Result::success)
                .orElseGet(() -> Result.error("Group with Id " + groupId + " not found"));
    }

    @Cacheable(value = "groupCurrentByKey", key = "#key")
    public Result<GroupDTO> getGroupCurrentByKey(String key) {
        return queryService.findGroupByKeyAndSemester(key, CURRENT_SEMESTER)
                .map(groupMappingService::mapGroupToDTOWithTeachers)
                .map(Result::success)
                .orElseGet(() -> Result.error("Group with Key " + key + " not found"));
    }

    public List<GroupDTO> getCurrentGroupsByClassroom(String classroom) {
        return queryService.findGroupsByClassroom(classroom).stream()
                .map(groupMappingService::mapGroupToDTOWithTeachers)
                .toList();
    }

    public Page<GroupDTO> getCurrentGroups(Pageable pageable) {
        return queryService.findBySchoolPeriodPageable(CURRENT_SEMESTER, pageable)
                .map(groupMappingService::mapGroupToDTOWithTeachers);
    }

    public List<GroupDTO> getCurrentGroupsByClassroomPrefix(char buildingLetter) {
        return List.of();
    }

    public Result<List<GroupDTO>> getGroupsByIds(List<Long> groupsId) {
        List<Group> groups = groupRepository.findByIdIn(groupsId);

        List<Long> foundGroupIds = groups.stream()
                .map(Group::getId)
                .toList();

        List<Long> missingIds = groupsId.stream()
                .filter(id -> !foundGroupIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            String errorMessage = "Groups not found for IDs: " + missingIds;
            return Result.error(errorMessage);
        }

        List<GroupDTO> groupDTOS = groups.stream()
                .map(groupMappingService::mapGroupToDTOWithTeachers)
                .toList();

        return Result.success(groupDTOS);
    }

    @Cacheable(value = "currentGroupsByObligatorySubjectId", key = "#subjectId")
    public List<GroupDTO> getCurrentGroupsByObligatorySubjectId(Long subjectId) {
        List<Group> groups = groupRepository.findBySubjectIdAndSubjectType(subjectId, SubjectType.OBLIGATORY);

        return groups.stream()
                .map(groupMappingService::mapGroupToDTOWithTeachers)
                .toList();
    }

    @Cacheable(value = "currentGroupsByElectiveSubjectId", key = "#subjectId")
    public List<GroupDTO> getCurrentGroupsByElectiveSubjectId(Long subjectId) {
        List<Group> groups = groupRepository.findBySubjectIdAndSubjectType(subjectId, SubjectType.ELECTIVE);

        return groups.stream()
                .map(groupMappingService::mapGroupToDTOWithTeachers)
                .toList();
    }

    @Cacheable(value = "currentGroupsByTeacherId", key = "#subjectId")
    public List<GroupDTO> getCurrentGroupsByTeacherId(Long subjectId) {
        List<Group> groups = groupRepository.findByTeacherIdAndSchoolPeriod(subjectId, CURRENT_SEMESTER);

        return groups.stream()
                .map(groupMappingService::mapGroupToDTOWithTeachers)
                .toList();
    }
}
