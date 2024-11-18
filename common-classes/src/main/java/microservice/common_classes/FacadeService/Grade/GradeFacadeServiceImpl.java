package microservice.common_classes.FacadeService.Grade;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Service("GradeFacadeServiceImpl")
public class GradeFacadeServiceImpl implements GradeFacadeService {

    private final RestTemplate restTemplate;
    private final Supplier<String> groupServiceUrlProvider;

    @Autowired
    public GradeFacadeServiceImpl(RestTemplate restTemplate,
                                  Supplier<String> groupServiceUrlProvider) {
        this.restTemplate = restTemplate;
        this.groupServiceUrlProvider = groupServiceUrlProvider;
    }

    @Override
    public CompletableFuture<GradeDTO> getGradeById(Long gradeId) {
        String gradeUrl = groupServiceUrlProvider.get() + "/v1/drugstore/grades/" + gradeId;

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
    public CompletableFuture<List<GradeDTO>> getGradesByStudentId(Long studentId) {
        String gradeUrl = groupServiceUrlProvider.get() + "/v1/drugstore/grades/student-history/" + studentId;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<List<GradeDTO>>> responseEntity = restTemplate.exchange(
                        gradeUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<List<GradeDTO>>>() {
                        }
                );

                log.info("getGradesByStudentId -> Student History Grades with ID: {} successfully fetched", studentId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getGradeById -> Student with ID {} not found: {}", studentId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getGradesByStudentId -> Error fetching group with ID {}: {}", studentId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
