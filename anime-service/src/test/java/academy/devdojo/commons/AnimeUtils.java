package academy.devdojo.commons;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> newAnimeList() {
        var jujutsuKaisen = Anime.builder().id(1L).name("Jujutsu Kaisen").build();
        var bokuNoHero = Anime.builder().id(2L).name("Boku no Hero").build();
        var onePiece = Anime.builder().id(3L).name("One Piece").build();
        return new ArrayList<>(List.of(jujutsuKaisen, bokuNoHero, onePiece));
    }
}
