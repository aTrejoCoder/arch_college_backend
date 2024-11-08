package microservice.schedule_service.Service;

import jakarta.persistence.EntityNotFoundException;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.DTO.GroupDTO;
import microservice.schedule_service.DTO.GroupInsertDTO;
import microservice.schedule_service.DTO.GroupUpdateDTO;
import microservice.schedule_service.DTO.ScheduleInsertDTO;
import microservice.schedule_service.Models.Schedule;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final SubjectFacadeService subjectFacadeService;
    private final GroupMapper groupMapper;
    String STATIC_NUMBER_ORDINARY_START_KEY = "5";
    String STATIC_NUMBER_ELECTIVE_START_KEY = "6";


    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            SubjectFacadeService subjectFacadeService,
                            GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.subjectFacadeService = subjectFacadeService;
        this.groupMapper = groupMapper;
    }

    @Override
    public Result<GroupDTO> getGroupById(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        return optionalGroup.map(group -> Result.success(groupMapper.entityToDTO(group))
        ).orElseGet(() -> Result.error("Group with Id " + groupId + " not found") );
    }

    @Override
    public Result<GroupDTO> getGroupByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKey(key);
        return optionalGroup.map(group -> Result.success(groupMapper.entityToDTO(group))
        ).orElseGet(() -> Result.error("Group with Key " + key + " not found") );
    }

    @Override
    public List<GroupDTO> getGroupsBySubjectId(Long subjectId) {
        List<Group> groups = groupRepository.findByOrdinarySubjectId(subjectId);
        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    public List<GroupDTO> getGroupsByTeacherId(Long subjectId) {
        List<Group> groups = groupRepository.findByTeacherId(subjectId);
        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    public void createGroup(GroupInsertDTO groupInsertDTO) {
        Group group = groupMapper.insertDtoToEntity(groupInsertDTO);
        group.setCurrentSemester();
        group.setKey(generateKey(group));

        groupRepository.save(group);
    }

    @Override
    public void updateGroup(GroupUpdateDTO groupUpdateDTO) {
        Group group = groupMapper.updateDtoToEntity(groupUpdateDTO);
        groupRepository.save(group);
    }

    @Override
    public void deleteGroupByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKey(key);
        if (optionalGroup.isEmpty()) {
            throw new EntityNotFoundException("Group with Key " + key + " not found");
        }

        groupRepository.delete(optionalGroup.get());
    }

    /* Domain Service */

    @Override
    public Result<Void> validateGroupSchedule(String classroom, List<ScheduleInsertDTO> schedules) {
        // Fetch existing groups in the same classroom
        List<Group> existingGroups = groupRepository.findByClassroom(classroom);

        for (ScheduleInsertDTO newSchedule : schedules) {
            for (Group group : existingGroups) {
                for (Schedule existingSchedule : group.getSchedule()) {
                    // Check if days match
                    if (existingSchedule.getDay().equals(newSchedule.getDay())) {
                        // Check if time ranges overlap
                        if (isTimeRangeOverlapping(
                                newSchedule.getStartTime(),
                                newSchedule.getEndTime(),
                                existingSchedule.getTimeRange().start(),
                                existingSchedule.getTimeRange().end()
                        )) {
                            return Result.error("Schedule conflict detected with another group in the same classroom.");
                        }
                    }
                }
            }
        }
        return Result.success(null);
    }

    private boolean isTimeRangeOverlapping(LocalTime newStart, LocalTime newEnd, LocalTime existingStart, LocalTime existingEnd) {
        return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart));
    }


    private String generateKey(Group group) {
        if (group.getElectiveSubjectId() != null && group.getOrdinarySubjectId() == null) {
            return generateElectiveKey(group);
        } else if (group.getOrdinarySubjectId() != null && group.getElectiveSubjectId() == null) {
            return generateOrdinaryKey(group);
        } else  {
            throw new RuntimeException("Invalid request, only one subject id allowed");
        }
    }

    // Ordinary Key example --> 5210
    // (5) means a generic number
    // (2) means the number of the semester that the semester that subject belongs
    // (10) means the number of the group from some subject
    private String generateOrdinaryKey(Group group) {
        List<Group> ordinaryGroups = groupRepository.findByOrdinarySubjectId(group.getOrdinarySubjectId());

        var subject = subjectFacadeService.getOrdinarySubject(group.getOrdinarySubjectId());
        int semesterNumber = subject.semester();

        int subjectGroupNumber = ordinaryGroups.size() + 1;

        return String.format("%s%d%02d", STATIC_NUMBER_ORDINARY_START_KEY, semesterNumber, subjectGroupNumber);
    }

    // Ordinary Key example --> 6102
    // (6) means a generic number
    // (10) means the ID of the elective subject
    // (9) means the number of the group from the elective subject
    private String generateElectiveKey(Group group) {
        List<Group> electiveGroups = groupRepository.findByElectiveSubjectId(group.getElectiveSubjectId());

        var subject = subjectFacadeService.getElectiveSubject(group.getElectiveSubjectId());

        int subjectGroupNumber = electiveGroups.size() + 1;

        return String.format("%s%02d%d", STATIC_NUMBER_ELECTIVE_START_KEY, subject.id(), subjectGroupNumber);
    }
}

