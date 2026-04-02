package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    {
        animes.add(Anime.builder().id(1L).name("Dragon Ball").build());
        animes.add(Anime.builder().id(2L).name("Kimetsu no Yaiba").build());
        animes.add(Anime.builder().id(3L).name("Re:Zero").build());
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
