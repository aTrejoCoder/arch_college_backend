package microservice.common_classes.FacadeService.Group;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.GroupNamedDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Service("GroupFacadeServiceImpl")
public class GroupFacadeServiceImpl implements GroupFacadeService {

    private final RestTemplate restTemplate;
    private final Supplier<String> groupServiceUrlProvider;

    @Autowired
    public GroupFacadeServiceImpl(RestTemplate restTemplate, Supplier<String> groupServiceUrlProvider) {
        this.restTemplate = restTemplate;
        this.groupServiceUrlProvider = groupServiceUrlProvider;
    }

    @Override
    public CompletableFuture<Boolean> validateExisitingGroup(Long groupId) {
        String groupUrl = groupServiceUrlProvider.get() + "/v1/drugstore/groups/" + groupId + "/validate";

        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                    groupUrl,
                    HttpMethod.GET,
                    null,
                    Boolean.class
            );

            return Objects.requireNonNull(responseEntity.getBody());
        });
    }


    @Override
    @Async("taskExecutor")
    public CompletableFuture<GroupDTO> getGroupById(Long groupId) {
        String groupUrl = groupServiceUrlProvider.get() + "/v1/drugstore/groups/" + groupId;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<GroupDTO>> responseEntity = restTemplate.exchange(
                        groupUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<GroupDTO>>() {}
                );

                log.info("Group with ID: {} successfully fetched", groupId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("Group with ID {} not found: {}", groupId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("Error fetching group with ID {}: {}", groupId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Result<Void>> reduceSpot(Long groupId) {
        String groupUrl = groupServiceUrlProvider.get() + "/v1/drugstore/groups/" + groupId;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<Result<Void>>> responseEntity = restTemplate.exchange(
                        groupUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<Result<Void>>>() {}
                );

                log.info("reduceSpot -> Group with ID: {} successfully fetched", groupId);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("reduceSpot ->  Group with ID {} not found: {}", groupId, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("reduceSpot ->  Error fetching group with ID {}: {}", groupId, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<List<GroupNamedDTO>> getGroupByStudentAccountNumber(String accountNumber) {
        return null;
    }
}
