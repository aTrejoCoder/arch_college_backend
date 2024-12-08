package microservice.schedule_service.Service.GroupServices.Implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.GroupRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupSpotsService {

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;
    private final String currentSemester = AcademicData.getCurrentSchoolPeriod();

    public Result<Void> decreaseSpot(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group with ID " + groupId + " not found"));

        if (group.getAvailableSpots() <= 0) {
            log.info("Attempted to decrease spots for group with ID {} but group is full, no available spots", groupId);
            return Result.error("Full group, no available spots");
        }

        group.decreaseAvailableSpot();
        groupRepository.save(group);
        log.info("Decreased available spots for group with ID {}", groupId);

        return Result.success();
    }

    public void increaseSpot(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group with ID " + groupId + " not found"));

        group.increaseAvailableSpot();
        groupRepository.save(group);
        log.info("Increased available spots for group with ID {}", groupId);
    }

    public Result<Void> validateSpotIncrease(int spotsToAdd) {
        if (spotsToAdd > 10) {
            log.info("Attempted to increase available spots by {} but it exceeds the maximum allowed (10)", spotsToAdd);
            return Result.error("availableSpots can't increase can't be above 10 ");
        } else {
            log.info("Spots increase validated successfully with {} spots to add", spotsToAdd);
            return Result.success();
        }
    }

    public GroupDTO addSpots(Long groupId, int spotsToAdd) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group with ID " + groupId + " not found"));

        group.increaseSpots(spotsToAdd);
        groupRepository.saveAndFlush(group);

        log.info("Added {} spots to group with ID {}", spotsToAdd, groupId);

        return groupMapper.entityToDTO(group);
    }
}
