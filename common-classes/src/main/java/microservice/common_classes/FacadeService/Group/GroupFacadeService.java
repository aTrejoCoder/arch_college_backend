package microservice.common_classes.FacadeService.Group;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.Result;

import java.util.concurrent.CompletableFuture;

public interface GroupFacadeService {
    CompletableFuture<Boolean> validateExisitingGroup(Long groupId);
    CompletableFuture<GroupDTO> getGroupById(Long groupId);
    CompletableFuture<Result<Void>> reduceSpot(Long groupId);
}
