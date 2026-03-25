package academy.devdojo.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProducerPostRequest {
    private String name;
}
