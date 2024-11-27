package microservice.common_classes.FacadeService.Group;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Response.ResponseWrapper;
import microservice.common_classes.Utils.Response.Result;
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
        String groupUrl = groupServiceUrlProvider.get() + "/v1/api/groups/" + groupId + "/validate";

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
        String groupUrl = groupServiceUrlProvider.get() + "/v1/api/groups/" + groupId;

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
    public CompletableFuture<GroupDTO> getCurrentGroupByKey(String groupKey) {
        String groupUrl = groupServiceUrlProvider.get() + "/v1/api/groups/key/" + groupKey;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<GroupDTO>> responseEntity = restTemplate.exchange(
                        groupUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<GroupDTO>>() {}
                );

                log.info("getCurrentGroupByKey -> Group with Key: {} successfully fetched", groupKey);

                return Objects.requireNonNull(responseEntity.getBody()).getData();
            } catch (EntityNotFoundException e) {
                log.warn("getCurrentGroupByKey -> Group with Key {} not found: {}", groupKey, e.getMessage());
                return null;
            } catch (Exception e) {
                log.error("getCurrentGroupByKey -> Error fetching group with Key {}: {}", groupKey, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Result<Void>> takeSpot(String groupKey) {
        String groupUrl = groupServiceUrlProvider.get() + "/v1/api/groups/" + groupKey + "/decrease-spot";
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<Void>> responseEntity = restTemplate.exchange(
                        groupUrl,
                        HttpMethod.PUT,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<Void>>() {}
                );

                 ResponseWrapper<Void> responseWrapper = Objects.requireNonNull(responseEntity.getBody());
                 if (!responseWrapper.isSuccess()) {
                     log.info("takeSpot -> Spot for group with Key: {} can't be reduced", groupKey);
                     return Result.error(responseWrapper.getMessage());
                 }

                log.info("takeSpot -> Spot for group with Key: {} successfully reduced", groupKey);
                return Result.success();


            } catch (Exception e) {
                log.error("takeSpot ->  Error fetching reducing spot for group with Key {}: {}", groupKey, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Result<Void>> returnSpot(String groupKey) {
        String groupUrl = groupServiceUrlProvider.get() + "/v1/api/groups/" + groupKey + "/increase-spot";

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<Void>> responseEntity = restTemplate.exchange(
                        groupUrl,
                        HttpMethod.PUT,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<Void>>() {}
                );

                Objects.requireNonNull(responseEntity.getBody());

                return Result.success();
            } catch (Exception e) {
                log.error("reduceSpot ->  Error fetching increase spot for group with Key {}: {}", groupKey, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Result<List<GroupDTO>>> getGroupsByIds(List<Long> idList) {
        String groupUrl = groupServiceUrlProvider.get() + "/v1/api/groups/by-ids/" + idList;

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<ResponseWrapper<List<GroupDTO>>> responseEntity = restTemplate.exchange(
                        groupUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseWrapper<List<GroupDTO>>>() {}
                );
                var responseWrapper = Objects.requireNonNull(responseEntity.getBody());

                if (!responseWrapper.isSuccess()) {
                    return Result.error(responseWrapper.getMessage());
                }

                log.info("getGroupsByIds -> Groups with IDs: {} successfully fetched", idList);
                return  Result.success(responseWrapper.getData());
            } catch (Exception e) {
                log.error("Error fetching groups with IDs {}: {}", idList, e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
