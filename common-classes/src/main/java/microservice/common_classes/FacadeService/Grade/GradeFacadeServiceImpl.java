
package microservice.common_classes.FacadeService.Grade;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.CustomPage;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Service("GradeFacadeServiceImpl")
public class GradeFacadeServiceImpl implements GradeFacadeService {

    private final RestTemplate restTemplate;
    private final Supplier<String> gradeServiceUrlProvider;

    @Autowired
    public GradeFacadeServiceImpl(RestTemplate restTemplate,
                                  @Qualifier("gradeServiceUrlProvider") Supplier<String> gradeServiceUrlProvider) {
        this.restTemplate = restTemplate;
        this.gradeServiceUrlProvider = gradeServiceUrlProvider;
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<GradeDTO> getGradeById(Long gradeId) {
        String gradeUrl = gradeServiceUrlProvider.get() + "/v1/api/grades/" + gradeId;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<GradeDTO>> responseEntity = restTemplate.exchange(
                        gradeUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<GradeDTO>>() {}
                );

                log.info("getGradeById -> Group with ID: {} successfully fetched", gradeId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getGradeById -> Group with ID {} not found: {}", gradeId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getGradeById -> Error fetching group with ID {}: {}", gradeId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<GradeDTO>> getGradesByStudentAccountNumber(String accountNumber) {
        String gradeUrl = gradeServiceUrlProvider.get() + "/v1/api/grades/student/" + accountNumber +  "/all";

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<List<GradeDTO>>> responseEntity = restTemplate.exchange(
                        gradeUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<List<GradeDTO>>>() {
                        }
                );

                log.info("getGradesByStudentId -> {}", responseEntity.getBody());
                log.info("getGradesByStudentId -> Student History Grades with ID: {} successfully fetched", accountNumber);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getGradeById -> Student with ID {} not found: {}", accountNumber, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getGradesByStudentId -> Error fetching group with ID {}: {}", accountNumber, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CustomPage<GradeDTO> getGradesByCareerPageable(int page, int pageSize) {
        String baseUrl = gradeServiceUrlProvider.get() + "/v1/api/groups/by-career/1/pageable";
        String url = String.format("%s?page=%d&size=%d", baseUrl, page, pageSize);

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseWrapper<CustomPage<GradeDTO>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseWrapper<CustomPage<GradeDTO>>>() {}
        );
        return Objects.requireNonNull(response.getBody()).getData();
    }


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
