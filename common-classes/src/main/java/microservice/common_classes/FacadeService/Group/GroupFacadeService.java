package microservice.common_classes.FacadeService.Group;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GroupFacadeService {
    CompletableFuture<Boolean> validateExisitingGroup(Long groupId);
    CompletableFuture<GroupDTO> getGroupById(Long groupId);
    CompletableFuture<GroupDTO> getCurrentGroupByKey(String groupKey);
    CompletableFuture<Result<Void>> takeSpot(String groupKey);
    CompletableFuture<Result<Void>> returnSpot(String groupKey);
    CompletableFuture<Result<List<GroupDTO>>> getGroupsByIds(List<Long> idList);
}
