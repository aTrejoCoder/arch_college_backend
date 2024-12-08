package microservice.common_classes.FacadeService.AcademicCurriculumService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.SubjectSeriesDTO;
import microservice.common_classes.Utils.CustomPage;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Service("AcademicCurriculumFacadeServiceImpl")
public class AcademicCurriculumFacadeServiceImpl implements AcademicCurriculumFacadeService {

    private final RestTemplate restTemplate;
    private final Supplier<String> academicCurriculumServiceUrlProvider;

    @Autowired
    public AcademicCurriculumFacadeServiceImpl(RestTemplate restTemplate,
                                               @Qualifier("academicCurriculumServiceUrlProvider") Supplier<String> academicCurriculumServiceUrlProvider) {
        this.restTemplate = restTemplate;
        this.academicCurriculumServiceUrlProvider = academicCurriculumServiceUrlProvider;
    }

    @Override
    public CompletableFuture<ObligatorySubjectDTO> getOrdinarySubjectById(Long subjectId) {
        String subjectUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/subjects/obligatory/" + subjectId;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<ObligatorySubjectDTO>> responseEntity = restTemplate.exchange(
                        subjectUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<ObligatorySubjectDTO>>() {}
                );

                log.info("getOrdinarySubjectById -> OrdinarySubject with ID: {} successfully fetched", subjectId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e ) {
                log.warn("getOrdinarySubjectById -> OrdinarySubject with ID {} not found: {}", subjectId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getOrdinarySubjectById -> Error fetching subject with ID {}: {}", subjectId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<CareerDTO> getCareerById(Long careerId) {
        String subjectUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/careers/" + careerId;
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<CareerDTO>> responseEntity = restTemplate.exchange(
                        subjectUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<CareerDTO>>() {}
                );

                log.info("getCareerById -> Career with ID: {} successfully fetched", careerId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getCareerById -> Career with ID {} not found: {}", careerId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getCareerById -> Error fetching Career with ID {}: {}", careerId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<ProfessionalLineDTO> getProfessionalLineById(Long professionalLineId) {
        String subjectUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/careers/" + professionalLineId;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<ProfessionalLineDTO>> responseEntity = restTemplate.exchange(
                        subjectUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<ProfessionalLineDTO>>() {}
                );

                log.info("getProfessionalLineById -> Professional Line with ID: {} successfully fetched", professionalLineId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getProfessionalLineById -> Professional Line with ID {} not found: {}", professionalLineId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getProfessionalLineById -> Error fetching Professional Line with ID {}: {}", professionalLineId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<ElectiveSubjectDTO> getElectiveSubjectById(Long subjectId) {
        String subjectUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/subjects/electives/" + subjectId;
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

    @Override
    public CompletableFuture<List<ObligatorySubjectDTO>> getObligatorySubjectsByCareer(String careerId) {
        String subjectUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/subjects/electives/by-career/" + careerId;
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<List<ObligatorySubjectDTO>>> responseEntity = restTemplate.exchange(
                        subjectUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<List<ObligatorySubjectDTO>>>() {}
                );

                log.info("getObligatorySubjectsByCareer -> ElectiveSubject with Career ID: {} successfully fetched", careerId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getObligatorySubjectsByCareer -> ElectiveSubject with Career ID {} not found: {}", careerId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getObligatorySubjectsByCareer -> Error fetching subject with Career ID {}: {}", careerId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public CompletableFuture<List<ElectiveSubjectDTO>> getElectiveSubjectsByCareer(Long careerId) {
        String subjectUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/subjects/electives/by-career/" + careerId;
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<List<ElectiveSubjectDTO>>> responseEntity = restTemplate.exchange(
                        subjectUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<List<ElectiveSubjectDTO>>>() {
                        }
                );

                log.info("getElectiveSubjectsByCareer -> ElectiveSubject with Career ID: {} successfully fetched", careerId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getElectiveSubjectsByCareer -> ElectiveSubject with Career ID {} not found: {}", careerId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getElectiveSubjectsByCareer -> Error fetching subject with Career ID {}: {}", careerId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CustomPage<ObligatorySubjectDTO> getObligatorySubjectsPageable(int page, int size) {
        String baseUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/subjects/obligatory/all";
        String url = String.format("%s?page=%d&size=%d", baseUrl, page, size);

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseWrapper<CustomPage<ObligatorySubjectDTO>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseWrapper<CustomPage<ObligatorySubjectDTO>>>() {}
        );
        return Objects.requireNonNull(response.getBody()).getData();
    }

    @Override
    public CustomPage<ElectiveSubjectDTO> getElectiveSubjectsPageable(int page, int size) {
        String baseUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/subjects/electives/all";
        String url = String.format("%s?page=%d&size=%d", baseUrl, page, size);

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseWrapper<CustomPage<ElectiveSubjectDTO>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseWrapper<CustomPage<ElectiveSubjectDTO>>>() {}
        );
        return Objects.requireNonNull(response.getBody()).getData();
    }

    @Override
    public CustomPage<SubjectSeriesDTO> getSubjectSeriesPageable(int page, int pageSize) {
        String baseUrl = academicCurriculumServiceUrlProvider.get() + "/v1/api/academic_curriculum/subject-series/all";
        String url = String.format("%s?page=%d&size=%d", baseUrl, page, pageSize);

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseWrapper<CustomPage<SubjectSeriesDTO>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseWrapper<CustomPage<SubjectSeriesDTO>>>() {}
        );
        return Objects.requireNonNull(response.getBody()).getData();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
