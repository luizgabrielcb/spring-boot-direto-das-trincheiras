package academy.devdojo.anime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePostResponse {
    private Long id;
    private String name;
}