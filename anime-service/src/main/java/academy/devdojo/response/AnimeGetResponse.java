package academy.devdojo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AnimeGetResponse {
    private Long id;
    private String name;
}
