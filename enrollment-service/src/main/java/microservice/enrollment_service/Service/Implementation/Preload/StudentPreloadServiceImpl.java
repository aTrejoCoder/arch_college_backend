package microservice.enrollment_service.Service.Implementation.Preload;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.FacadeService.Student.StudentFacadeService;
import microservice.common_classes.Utils.CustomPage;
import microservice.enrollment_service.Mappers.StudentMapper;
import microservice.enrollment_service.Model.Preload.Student;
import microservice.enrollment_service.Repository.StudentRepository;
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
public class StudentPreloadServiceImpl implements PreloadDataService<Student> {

    private final StudentFacadeService studentFacadeService;
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final Map<String, String> processStatus = new ConcurrentHashMap<>();

    @Autowired
    public StudentPreloadServiceImpl(@Qualifier("StudentFacadeServiceImpl") StudentFacadeService studentFacadeService,
                                     StudentMapper studentMapper, StudentRepository studentRepository) {
        this.studentFacadeService = studentFacadeService;
        this.studentMapper = studentMapper;
        this.studentRepository = studentRepository;
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
        int pageSize = 20;
        processStatus.put(processId, "Processing");

        try {
            List<Student> allStudents = getAllStudents(pageSize);

            saveStudents(allStudents);
            processStatus.put(processId, "Completed");

            log.info("Preloaded {} schedules into enrollment-service", allStudents.size());
        } catch (Exception e) {
            processStatus.put(processId, "Failed");

            log.error("Failed to preload schedules: {}", e.getMessage());
        }
    }

    @Override
    public void clear() {
        studentRepository.deleteAll();
    }

    private List<Student> getAllStudents(int pageSize)  {
        int page = 0;
        boolean hasMorePages = true;
        List<Student> allStudents = new ArrayList<>();

        while (hasMorePages) {
            CustomPage<StudentDTO> studentPage = studentFacadeService.getStudentsPageable(page, pageSize);
            List<Student> students = studentPage.getContent().stream()
                    .map(studentMapper::dtoToEntity)
                    .toList();
            allStudents.addAll(students);

            hasMorePages = studentPage.hasNext();
            page++;
        }

        return allStudents;
    }

    private void saveStudents(List<Student> students) {
        studentRepository.deleteAll();
        studentRepository.saveAll(students);
    }
}
