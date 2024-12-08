package microservice.enrollment_service.Service.Implementation.Preload;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.SubjectSeriesDTO;
import microservice.common_classes.FacadeService.AcademicCurriculumService.AcademicCurriculumFacadeService;
import microservice.common_classes.Models.Subject;
import microservice.common_classes.Utils.CustomPage;
import microservice.enrollment_service.Mappers.ElectiveSubjectMapper;
import microservice.enrollment_service.Mappers.ObligatorySubjectMapper;
import microservice.enrollment_service.Mappers.SubjectSeriesMapper;
import microservice.enrollment_service.Model.Preload.ElectiveSubject;
import microservice.enrollment_service.Model.Preload.ObligatorySubject;
import microservice.enrollment_service.Model.Preload.SubjectSeries;
import microservice.enrollment_service.Repository.ElectiveSubjectRepository;
import microservice.enrollment_service.Repository.ObligatorySubjectRepository;
import microservice.enrollment_service.Repository.SubjectSeriesRepository;
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

    private final AcademicCurriculumFacadeService obligatoryAcademicCurriculumFacadeService;
    private final ObligatorySubjectRepository obligatorySubjectRepository;
    private final ElectiveSubjectRepository electiveSubjectRepository;
    private final ObligatorySubjectMapper obligatorySubjectMapper;
    private final ElectiveSubjectMapper electiveSubjectMapper;
    private final SubjectSeriesMapper subjectSeriesMapper;
    private final SubjectSeriesRepository subjectSeriesRepository;
    private final Map<String, String> processStatus = new ConcurrentHashMap<>();

    @Autowired
    public SubjectPreloadServiceImpl(@Qualifier("AcademicCurriculumFacadeServiceImpl") AcademicCurriculumFacadeService obligatoryAcademicCurriculumFacadeService,
                                     ObligatorySubjectRepository obligatorySubjectRepository,
                                     ElectiveSubjectRepository electiveSubjectRepository,
                                     ObligatorySubjectMapper obligatorySubjectMapper,
                                     ElectiveSubjectMapper electiveSubjectMapper, SubjectSeriesMapper subjectSeriesMapper, SubjectSeriesRepository subjectSeriesRepository) {
        this.obligatoryAcademicCurriculumFacadeService = obligatoryAcademicCurriculumFacadeService;
        this.obligatorySubjectRepository = obligatorySubjectRepository;
        this.electiveSubjectRepository = electiveSubjectRepository;
        this.obligatorySubjectMapper = obligatorySubjectMapper;
        this.electiveSubjectMapper = electiveSubjectMapper;
        this.subjectSeriesMapper = subjectSeriesMapper;
        this.subjectSeriesRepository = subjectSeriesRepository;
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
            List<SubjectSeries> subjectSeries = getAllSubjectsSerialization(pageSize);

            saveSubjects(obligatorySubjects, electiveSubjects, subjectSeries);
            processStatus.put(processId, "Completed");

            log.info("Preloaded {} subjects into enrollment-service", obligatorySubjects.size() + electiveSubjects.size());
        } catch (Exception e) {
            processStatus.put(processId, "Failed");

            log.error("Failed to preload subjects: {}", e.getMessage());
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
            CustomPage<ObligatorySubjectDTO> obligatorySubjectPage = obligatoryAcademicCurriculumFacadeService.getObligatorySubjectsPageable(page, pageSize);

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

    private List<SubjectSeries> getAllSubjectsSerialization(int pageSize)  {
        int page = 0;
        boolean hasMorePages = true;
        List<SubjectSeries> allSubjects = new ArrayList<>();

        while (hasMorePages) {
            CustomPage<SubjectSeriesDTO> obligatorySubjectPage = obligatoryAcademicCurriculumFacadeService.getSubjectSeriesPageable(page, pageSize);

            List<SubjectSeries> obligatorySubjects = obligatorySubjectPage.getContent()
                    .stream()
                    .map(subjectSeriesMapper::dtoToEntity)
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
            CustomPage<ElectiveSubjectDTO> obligatorySubjectPage = obligatoryAcademicCurriculumFacadeService.getElectiveSubjectsPageable(page, pageSize);

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

    private void saveSubjects(List<ObligatorySubject> obligatorySubjects, List<ElectiveSubject> electiveSubjects, List<SubjectSeries> subjectSeries) {
        obligatorySubjectRepository.deleteAll();
        electiveSubjectRepository.deleteAll();
        subjectSeriesRepository.deleteAll();

        obligatorySubjectRepository.saveAll(obligatorySubjects);
        electiveSubjectRepository.saveAll(electiveSubjects);
        subjectSeriesRepository.saveAll(subjectSeries);
    }
}
