package microservice.subject_service.DTOs.SocialNetwork;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.subject_service.Model.SocialNetworkName;

@Data
@NoArgsConstructor
public class SocialNetworkInsertDTO {
    @Enumerated(EnumType.STRING)
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be empty")
    private SocialNetworkName name;

    @JsonProperty("career_id")
    @NotNull(message = "career_id can't be null")
    @Positive(message = "career_id can't be negative")
    private Long careerId;
}
