package academy.devdojo.producer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProducerGetResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
