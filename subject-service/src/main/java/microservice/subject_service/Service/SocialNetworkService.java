package microservice.subject_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.SocialNetwork.SocialNetworkDTO;
import microservice.subject_service.DTOs.SocialNetwork.SocialNetworkInsertDTO;

import java.util.List;

public interface SocialNetworkService {
    Result<SocialNetworkDTO> getSocialNetworkById(Long socialNetworkId);
    List<SocialNetworkDTO> getSocialNetworksByCareerId(Long socialNetworkId);
    void createSocialNetwork(SocialNetworkInsertDTO socialNetworkInsertDTO);
    void updateSocialNetwork(SocialNetworkInsertDTO socialNetworkInsertDTO);
    void deleteSocialNetwork(Long socialNetworkId);
}
