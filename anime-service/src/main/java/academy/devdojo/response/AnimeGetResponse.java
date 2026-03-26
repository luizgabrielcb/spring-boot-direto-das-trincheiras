package academy.devdojo.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnimeGetResponse {
    private Long id;
    private String name;
}
