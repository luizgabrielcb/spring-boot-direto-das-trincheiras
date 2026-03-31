package academy.devdojo.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProducerPostResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}