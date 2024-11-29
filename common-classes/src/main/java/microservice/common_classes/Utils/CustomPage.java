package microservice.common_classes.Utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomPage<T> {
    @JsonProperty("content")
    private List<T> content = new ArrayList<>();;

    @JsonProperty("number")
    private int number;

    @JsonProperty("size")
    private int size;

    @JsonProperty("totalPages")
    private int totalPages;

    @JsonProperty("totalElements")
    private long totalElements;

    public boolean hasNext() {
        return number + 1 < totalPages;
    }
}
