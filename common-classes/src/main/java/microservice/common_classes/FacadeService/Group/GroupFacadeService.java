package microservice.common_classes.FacadeService.Group;

import microservice.common_classes.DTOs.Group.GroupDTO;

import java.util.concurrent.CompletableFuture;

public interface GroupFacadeService {
    CompletableFuture<Boolean> validateExisitingGroup(Long groupId);
    CompletableFuture<GroupDTO> getGroupById(Long groupId);
}
