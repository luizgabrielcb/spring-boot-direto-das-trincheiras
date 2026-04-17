package academy.devdojo.anime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnimeGetResponse {
    private Long id;
    private String name;
}
