package microservice.enrollment_service.Service.Implementation.Preload;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.FacadeService.Grade.GradeFacadeService;
import microservice.common_classes.Utils.CustomPage;
import microservice.enrollment_service.Mappers.GradeMapper;
import microservice.enrollment_service.Model.Preload.Grade;
import microservice.enrollment_service.Repository.GradeRepository;
import microservice.enrollment_service.Service.PreloadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class GradePreloadServiceImpl implements PreloadDataService<Grade> {

    private final GradeFacadeService gradeFacadeService;
    private final GradeMapper gradeMapper;
    private final GradeRepository gradeRepository;
    private final Map<String, String> processStatus = new ConcurrentHashMap<>();

    @Autowired
    public GradePreloadServiceImpl(@Qualifier("GradeFacadeServiceImpl") GradeFacadeService gradeFacadeService,
                                   GradeMapper gradeMapper, GradeRepository gradeRepository) {
        this.gradeFacadeService = gradeFacadeService;
        this.gradeMapper = gradeMapper;
        this.gradeRepository = gradeRepository;
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
            List<Grade> allGrades = getAllGrades(pageSize);

            saveGrades(allGrades);
            processStatus.put(processId, "Completed");

            log.info("Preloaded {} schedules into enrollment-service", allGrades.size());
        } catch (Exception e) {
            processStatus.put(processId, "Failed");

            log.error("Failed to preload schedules: {}", e.getMessage());
        }
    }

    @Override
    public void clear() {
        gradeRepository.deleteAll();
    }

    private List<Grade> getAllGrades(int pageSize)  {
        int page = 0;
        boolean hasMorePages = true;
        List<Grade> allGrades = new ArrayList<>();

        while (hasMorePages) {
            CustomPage<GradeDTO> gradePage = gradeFacadeService.getGradesByCareerPageable(page, pageSize);
            List<Grade> grades = gradePage.getContent().stream()
                    .map(gradeMapper::dtoToEntity)
                    .toList();
            allGrades.addAll(grades);

            hasMorePages = gradePage.hasNext();
            page++;
        }

        return allGrades;
    }

    private void saveGrades(List<Grade> grades) {
        gradeRepository.deleteAll();
        gradeRepository.saveAll(grades);
    }
}
