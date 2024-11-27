package microservice.schedule_service.Service.GroupServices;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Utils.GroupMappingService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupSpotsService {

    private final GroupMappingService mappingService;
    private final GroupRepository groupRepository;
    private final String currentSemester = SemesterData.getCurrentSchoolPeriod();

    public Result<Void> decreaseSpot(String key) {
        Group group = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + key + " not found"));

        if (group.getAvailableSpots() <= 0 ) {
            return Result.error("Full group, no available spots");
        }

        group.decreaseAvailableSpot();
        groupRepository.save(group);

        return Result.success();
    }

    public void increaseSpot(String key) {
        Group group = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + key + " not found"));

        group.increaseAvailableSpot();
        groupRepository.save(group);
    }

    public Result<Void> validateSpotIncrease(int spotsToAdd) {
        if (spotsToAdd > 10) {
            return Result.error("availableSpots can't increase can't be above 10 ");
        } else {
            return Result.success();
        }
    }

    public GroupDTO addSpots(String key, int spotsToAdd) {
        Group group = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester)
                .orElseThrow(() -> new EntityNotFoundException("Group with Key " + key + " not found"));

        group.increaseSpots(spotsToAdd);

        groupRepository.saveAndFlush(group);

        return mappingService.mapGroupToDTOWithTeachers(group);
    }


}
