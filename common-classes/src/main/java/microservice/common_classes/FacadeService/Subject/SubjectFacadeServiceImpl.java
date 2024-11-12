package microservice.common_classes.FacadeService.Subject;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.Utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Service("subjectFacadeServiceImpl")
public class SubjectFacadeServiceImpl implements SubjectFacadeService {

    private final RestTemplate restTemplate;
    private final Supplier<String> subjectServiceUrlProvider;

    @Autowired
    public SubjectFacadeServiceImpl(RestTemplate restTemplate, Supplier<String> subjectServiceUrlProvider) {
        this.restTemplate = restTemplate;
        this.subjectServiceUrlProvider = subjectServiceUrlProvider;
    }

    @Override
    public CompletableFuture<Boolean> validateExistingOrdinarySubject(Long subjectId) {
        return null;
    }

    @Override
    public CompletableFuture<OrdinarySubjectDTO> getOrdinarySubjectById(Long subjectId) {
        String subjectUrl = subjectServiceUrlProvider.get() + "/v1/api/subjects/ordinaries/" + subjectId;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<OrdinarySubjectDTO>> responseEntity = restTemplate.exchange(
                        subjectUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<OrdinarySubjectDTO>>() {}
                );

                log.info("getOrdinarySubjectById -> OrdinarySubject with ID: {} successfully fetched", subjectId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getOrdinarySubjectById -> OrdinarySubject with ID {} not found: {}", subjectId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getOrdinarySubjectById -> Error fetching subject with ID {}: {}", subjectId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> validateExistingElectiveSubject(Long subjectId) {
        return null;
    }

    @Override
    public CompletableFuture<ElectiveSubjectDTO> getElectiveSubjectById(Long subjectId) {
        String subjectUrl = subjectServiceUrlProvider.get() + "/v1/api/subjects/electives/" + subjectId;
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> responseEntity = restTemplate.exchange(
                        subjectUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<ElectiveSubjectDTO>>() {}
                );

                log.info("getElectiveSubjectById -> ElectiveSubject with ID: {} successfully fetched", subjectId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getElectiveSubjectById -> ElectiveSubject with ID {} not found: {}", subjectId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getElectiveSubjectById -> Error fetching subject with ID {}: {}", subjectId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
