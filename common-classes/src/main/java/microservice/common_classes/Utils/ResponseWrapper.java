package microservice.common_classes.Utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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

    public enum StatusType {
        CREATED, UPDATED, DELETED, FOUND, NOT_FOUND, CONFLICT, BAD_REQUEST, ERROR;
    }

    private static String getDefaultMessage(StatusType type, String entity, String parameter, String parameterValue) {
        return switch (type) {
            case FOUND -> entity + " with " + parameter + " " + parameterValue + " successfully fetched";
            case NOT_FOUND -> entity + " with " + parameter + " " + parameterValue + " not found";
            case UPDATED -> entity + " successfully updated";
            case CREATED -> entity + " successfully created";
            case DELETED -> entity + " successfully deleted";
            case CONFLICT -> "Conflict occurred for " + entity;
            case BAD_REQUEST -> "Bad request for " + entity;
            case ERROR -> "Error processing " + entity;
        };
    }

    public static <T> ResponseWrapper<T> buildResponse(boolean success, T data, String message, HttpStatus status) {
        return new ResponseWrapper<>(success, data, message, status.value(), LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> buildResponse(StatusType type, String entity, String parameter, String parameterValue, T data) {
        HttpStatus status = switch (type) {
            case CREATED -> HttpStatus.CREATED;
            case UPDATED, DELETED, FOUND -> HttpStatus.OK;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        String message = getDefaultMessage(type, entity, parameter, parameterValue);
        boolean success = status.is2xxSuccessful();

        return new ResponseWrapper<>(success, data, message, status.value(), LocalDateTime.now());
    }
}
