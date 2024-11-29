package microservice.enrollment_service.Service.Implementation.Preload;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.Models.Subject;
import microservice.common_classes.Utils.CustomPage;
import microservice.enrollment_service.Mappers.ElectiveSubjectMapper;
import microservice.enrollment_service.Mappers.ObligatorySubjectMapper;
import microservice.enrollment_service.Model.Preload.ElectiveSubject;
import microservice.enrollment_service.Model.Preload.ObligatorySubject;
import microservice.enrollment_service.Repository.ElectiveSubjectRepository;
import microservice.enrollment_service.Repository.ObligatorySubjectRepository;
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
public class SubjectPreloadServiceImpl implements PreloadDataService<Subject> {

    private final SubjectFacadeService obligatorySubjectFacadeService;
    private final ObligatorySubjectRepository obligatorySubjectRepository;
    private final ElectiveSubjectRepository electiveSubjectRepository;
    private final ObligatorySubjectMapper obligatorySubjectMapper;
    private final ElectiveSubjectMapper electiveSubjectMapper;
    private final Map<String, String> processStatus = new ConcurrentHashMap<>();

    @Autowired
    public SubjectPreloadServiceImpl(@Qualifier("SubjectFacadeServiceImpl") SubjectFacadeService obligatorySubjectFacadeService,
                                     ObligatorySubjectRepository obligatorySubjectRepository,
                                     ElectiveSubjectRepository electiveSubjectRepository,
                                     ObligatorySubjectMapper obligatorySubjectMapper,
                                     ElectiveSubjectMapper electiveSubjectMapper) {
        this.obligatorySubjectFacadeService = obligatorySubjectFacadeService;
        this.obligatorySubjectRepository = obligatorySubjectRepository;
        this.electiveSubjectRepository = electiveSubjectRepository;
        this.obligatorySubjectMapper = obligatorySubjectMapper;
        this.electiveSubjectMapper = electiveSubjectMapper;
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
        int pageSize = 10;
        processStatus.put(processId, "Processing");

        try {
            List<ObligatorySubject> obligatorySubjects = getAllObligatorySubjects(pageSize);
            List<ElectiveSubject> electiveSubjects = getAllElectiveSubjects(pageSize);

            saveSubjects(obligatorySubjects, electiveSubjects);
            processStatus.put(processId, "Completed");

            log.info("Preloaded {} schedules into enrollment-service", obligatorySubjects.size() + electiveSubjects.size());
        } catch (Exception e) {
            processStatus.put(processId, "Failed");

            log.error("Failed to preload schedules: {}", e.getMessage());
        }
    }

    @Override
    public void clear() {
        obligatorySubjectRepository.deleteAll();
    }

    private List<ObligatorySubject> getAllObligatorySubjects(int pageSize)  {
        int page = 0;
        boolean hasMorePages = true;
        List<ObligatorySubject> allSubjects = new ArrayList<>();

        while (hasMorePages) {
            CustomPage<ObligatorySubjectDTO> obligatorySubjectPage = obligatorySubjectFacadeService.getObligatorySubjectsPageable(page, pageSize);

            List<ObligatorySubject> obligatorySubjects = obligatorySubjectPage.getContent()
                    .stream()
                    .map(obligatorySubjectMapper::dtoToEntity)
                    .toList();
            allSubjects.addAll(obligatorySubjects);

            hasMorePages = obligatorySubjectPage.hasNext();
            page++;
        }

        return allSubjects;
    }

    private List<ElectiveSubject> getAllElectiveSubjects(int pageSize)  {
        int page = 0;
        boolean hasMorePages = true;
        List<ElectiveSubject> allSubjects = new ArrayList<>();

        while (hasMorePages) {
            CustomPage<ElectiveSubjectDTO> obligatorySubjectPage = obligatorySubjectFacadeService.getElectiveSubjectsPageable(page, pageSize);

            List<ElectiveSubject> obligatorySubjects = obligatorySubjectPage.getContent()
                    .stream()
                    .map(electiveSubjectMapper::dtoToEntity)
                    .toList();
            allSubjects.addAll(obligatorySubjects);

            hasMorePages = obligatorySubjectPage.hasNext();
            page++;
        }

        return allSubjects;
    }

    private void saveSubjects(List<ObligatorySubject> obligatorySubjects, List<ElectiveSubject> electiveSubjects) {
        obligatorySubjectRepository.deleteAll();
        electiveSubjectRepository.deleteAll();

        obligatorySubjectRepository.saveAll(obligatorySubjects);
        electiveSubjectRepository.saveAll(electiveSubjects);
    }
}
