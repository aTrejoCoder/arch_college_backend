package microservice.common_classes.FacadeService;

import microservice.common_classes.DTOs.Group.GroupDTO;

public interface GroupFacadeService {
    boolean validateExisitingGroup(Long groupId);
    GroupDTO getGroupById(Long groupId);
}
