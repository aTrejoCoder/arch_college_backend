package microservice.user_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SignupDTO {
    @JsonProperty("account_number")
    @NotNull(message = "account_number cant' be null")
    @NotBlank(message = "account_number can't be blank")
    private String accountNumber;

    @JsonProperty("email")
    @NotNull(message = "email cant' be null")
    @NotBlank(message = "email can't be blank")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("password")
    @NotNull(message = "password cant' be null")
    @NotBlank(message = "password can't be blank")
    private String password;
}
