package microservice.common_classes.Utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Schema(description = "Custom API Response wrapper")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T> {
    @JsonProperty("success")
    @Schema(description = "Indicates if the operation was successful")
    private boolean success;

    @JsonProperty("data")
    @Schema(description = "Response payload")
    private T data;

    @JsonProperty("message")
    @Schema(description = "Response message")
    private String message;

    @JsonProperty("code")
    @Schema(description = "Response status code")
    private int code;

    @JsonProperty("time_stamp")
    @Schema(description = "Timestamp of the response", example = "2024-09-15T10:30:00")
    private LocalDateTime timestamp;


    public static <T> ResponseWrapper<T> ok(T data, String message) {
        return new ResponseWrapper<>(
                true,
                data,
                message,
                HttpStatus.OK.value(),
                LocalDateTime.now()
        );
    }

    public static <T> ResponseWrapper<T> error(String message, int code) {
        return new ResponseWrapper<>(
                false,
                null,
                message,
                code,
                LocalDateTime.now()
        );
    }

    public static <T> ResponseWrapper<T> notFound(String message) {
        return new ResponseWrapper<>(
                false,
                null,
                message,
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
    }

    public static <T> ResponseWrapper<T> badRequest(String message) {
        return new ResponseWrapper<>(
                false,
                null,
                message,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
    }

}
