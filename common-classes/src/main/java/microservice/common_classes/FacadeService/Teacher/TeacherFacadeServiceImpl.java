package microservice.common_classes.FacadeService.Teacher;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.common_classes.Utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
@Slf4j
public class TeacherFacadeServiceImpl implements TeacherFacadeService {

    private final RestTemplate restTemplate;
    private final Supplier<String> teacherServiceUrlProvider;

    @Autowired
    public TeacherFacadeServiceImpl(RestTemplate restTemplate, Supplier<String> teacherServiceUrlProvider) {
        this.restTemplate = restTemplate;
        this.teacherServiceUrlProvider = teacherServiceUrlProvider;
    }


    @Override
    public CompletableFuture<Boolean> validateExisitingTeacher(Long teacherId) {
        String teacherUrl = teacherServiceUrlProvider.get() + "/v1/drugstore/teachers/" + teacherId + "/validate";

        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                    teacherUrl,
                    HttpMethod.GET,
                    null,
                    Boolean.class
            );

            return Objects.requireNonNull(responseEntity.getBody());
        });
    }


    @Override
    @Async("taskExecutor")
    public CompletableFuture<TeacherDTO> getTeacherById(Long teacherId) {
        String teacherUrl = teacherServiceUrlProvider.get() + "/v1/drugstore/teachers/" + teacherId;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<TeacherDTO>> responseEntity = restTemplate.exchange(
                        teacherUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<TeacherDTO>>() {}
                );

                log.info("Teacher with ID: {} successfully fetched", teacherId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("Teacher with ID {} not found: {}", teacherId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("Error fetching teacher with ID {}: {}", teacherId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
