package academy.devdojo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class Anime {
    private Long id;
    private String name;
    @Getter
    private static List<Anime> animes = new ArrayList<>();

    static {
        animes.add(Anime.builder().id(1L).name("Dragon Ball").build());
        animes.add(Anime.builder().id(2L).name("Kimetsu no Yaiba").build());
        animes.add(Anime.builder().id(3L).name("Re:Zero").build());
    }

}
