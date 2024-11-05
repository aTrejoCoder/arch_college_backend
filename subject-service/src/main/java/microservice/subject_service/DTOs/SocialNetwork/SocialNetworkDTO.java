package microservice.subject_service.DTOs.SocialNetwork;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.subject_service.Model.SocialNetworkName;

@Data
@NoArgsConstructor
public class SocialNetworkDTO {
    @JsonProperty("social_network_id")
    private Long socialNetworkId;

    @Enumerated(EnumType.STRING)
    private SocialNetworkName name;

    @JsonProperty("career_id")
    private Long careerId;
}
