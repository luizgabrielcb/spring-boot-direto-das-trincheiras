package academy.devdojo.domain;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class Anime {
    private Long id;
    private String name;

    public static List<Anime> getAnimes() {
        List<Anime> animeList = new ArrayList<>();
        animeList.add(Anime.builder().id(1L).name("Dragon Ball").build());
        animeList.add(Anime.builder().id(2L).name("Kimetsu no Yaiba").build());
        animeList.add(Anime.builder().id(3L).name("Re:Zero").build());
        return animeList;
    }
}
