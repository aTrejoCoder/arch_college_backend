package microservice.common_classes.DTOs.User;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTO {
    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("password")
    private String password;
}