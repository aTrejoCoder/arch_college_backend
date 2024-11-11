package microservice.user_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.user_service.Model.Role;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("student_id")
    private Integer studentId;

    @JsonProperty("teacher_id")
    private Integer teacherId;

    private Set<Role> roles;
}
