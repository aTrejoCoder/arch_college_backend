package microservice.enrollment_service.Service.Implementation.Preload;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.FacadeService.Group.GroupFacadeService;
import microservice.common_classes.Utils.CustomPage;
import microservice.enrollment_service.Mappers.GroupMapper;
import microservice.enrollment_service.Model.Preload.Group;
import microservice.enrollment_service.Repository.GroupRepository;
import microservice.enrollment_service.Service.PreloadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SchedulePreloadServiceImpl implements PreloadDataService<Group> {

    private final GroupFacadeService groupFacadeService;
    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;
    private final Map<String, String> processStatus = new ConcurrentHashMap<>();

    @Autowired
    public SchedulePreloadServiceImpl(@Qualifier("GroupFacadeServiceImpl") GroupFacadeService groupFacadeService,
                                      GroupMapper groupMapper, GroupRepository groupRepository) {
        this.groupFacadeService = groupFacadeService;
        this.groupMapper = groupMapper;
        this.groupRepository = groupRepository;
    }

    @Override
    public void startPreload(String processId) {
        processStatus.put(processId, "Started");

        new Thread(() -> preload(processId)).start();
    }

    @Override
    public String getPreloadStatus(String processId) {
        return processStatus.get(processId);
    }

    @Override
    @Transactional
    public void preload(String processId) {
        int pageSize = 1;
        processStatus.put(processId, "Processing");

        try {
            List<Group> allGroups = getAllGroups(pageSize);

            saveGroups(allGroups);
            processStatus.put(processId, "Completed");

            log.info("Preloaded {} schedules into enrollment-service", allGroups.size());
        } catch (Exception e) {
            processStatus.put(processId, "Failed");

            log.error("Failed to preload schedules: {}", e.getMessage());
        }
    }

    @Override
    public void clear() {
        groupRepository.deleteAll();
    }

    private List<Group> getAllGroups(int pageSize)  {
        int page = 0;
        boolean hasMorePages = true;
        List<Group> allGroups = new ArrayList<>();

        while (hasMorePages) {
            CustomPage<GroupDTO> groupPage = groupFacadeService.getGroupsPageable(page, pageSize);
            List<Group> groups = groupPage.getContent().stream()
                    .map(groupMapper::dtoToEntity)
                    .toList();
            allGroups.addAll(groups);

            hasMorePages = groupPage.hasNext();
            page++;
        }

        return allGroups;
    }

    private void saveGroups(List<Group> groups) {
        groupRepository.deleteAll();
        groupRepository.saveAll(groups);
    }
}
