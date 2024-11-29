package microservice.common_classes.FacadeService.Student;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.Utils.CustomPage;
import microservice.common_classes.Utils.ProfessionalLineModality;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Service("StudentFacadeServiceImpl")
public class StudentFacadeServiceImpl implements StudentFacadeService {

    private final RestTemplate restTemplate;
    private final Supplier<String> studentServiceUrlProvider;

    @Autowired
    public StudentFacadeServiceImpl(RestTemplate restTemplate, Supplier<String> studentServiceUrlProvider) {
        this.restTemplate = restTemplate;
        this.studentServiceUrlProvider = studentServiceUrlProvider;
    }


    @Override
    @Async("taskExecutor")
    public CompletableFuture<Boolean> validateExisitingStudentAsync(String accountNumber) {
        String studentUrl = studentServiceUrlProvider.get() + "/v1/api/students/" + accountNumber + "/validate" ;

        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                    studentUrl,
                    HttpMethod.GET,
                    null,
                    Boolean.class
            );

            return Objects.requireNonNull(responseEntity.getBody());
        });
    }


    @Override
    @Async("taskExecutor")
    public CompletableFuture<StudentDTO> getStudentByAccountNumberAsync(String accountNumber) {
        String studentUrl = studentServiceUrlProvider.get() + "/v1/api/students/by-accountNumber/" + accountNumber;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<StudentDTO>> responseEntity = restTemplate.exchange(
                        studentUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<StudentDTO>>() {}
                );

                log.info("Student with accountNumber: {} successfully fetched", accountNumber);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("Student with accountNumber {} not found: {}", accountNumber, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("Error fetching student with accountNumber {}: {}", accountNumber, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> setProfessionalLineDataAsync(String studentAccount, Long professionalLineId, ProfessionalLineModality professionalLineModality) {
        String studentUrl = studentServiceUrlProvider.get() + "/v1/api/students/" + studentAccount +
                "/set-professionalLine/" + professionalLineId + "/modality/" + professionalLineModality;

        return CompletableFuture.runAsync(() -> {
            try {
                restTemplate.exchange(
                        studentUrl,
                        HttpMethod.GET,
                        null,
                        Void.class
                );

                log.info("Professional line data set for student: {}", studentAccount);
            } catch (Exception e) {
                log.error("Error setting professional line data for student {}: {}", studentAccount, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> increaseSemesterCompletedAsync(String studentAccount) {
        String studentUrl = studentServiceUrlProvider.get() + "/v1/api/students/" + studentAccount + "/increase-semester-completed";

        return CompletableFuture.runAsync(() -> {
            try {
                restTemplate.exchange(
                        studentUrl,
                        HttpMethod.GET,
                        null,
                        Void.class
                );

                log.info("Semester incremented for student: {}", studentAccount);
            } catch (Exception e) {
                log.error("Error incrementing semester for student {}: {}", studentAccount, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CustomPage<StudentDTO> getStudentsPageable(int page, int pageSize) {
        String baseUrl = studentServiceUrlProvider.get() + "/v1/api/students/current/pageable";
        String url = String.format("%s?page=%d&size=%d", baseUrl, page, pageSize);

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseWrapper<CustomPage<StudentDTO>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseWrapper<CustomPage<StudentDTO>>>() {}
        );
        return Objects.requireNonNull(response.getBody()).getData();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
