package microservice.schedule_service.Service.GroupServices;

import lombok.RequiredArgsConstructor;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupQueryService {

    private final GroupRepository groupRepository;
    private final String SCHOOL_PERIOD = SemesterData.getCurrentSchoolPeriod();

    public Optional<Group> findGroupById(Long groupId) {
        return groupRepository.findById(groupId);
    }

    public Optional<Group> findGroupByKey(String groupKey) {
        return groupRepository.findByKeyAndSchoolPeriod(groupKey, SCHOOL_PERIOD);
    }


    public Optional<Group> findGroupByKeyAndSemester(String key, String semester) {
        return groupRepository.findByKeyAndSchoolPeriod(key, semester);
    }

    public List<Group> findGroupsByClassroom(String classroom) {
        return groupRepository.findByClassroom(classroom);
    }
}

