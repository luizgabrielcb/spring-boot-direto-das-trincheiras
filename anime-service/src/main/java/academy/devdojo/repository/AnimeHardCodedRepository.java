package academy.devdojo.repository;

import academy.devdojo.domain.Anime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimeHardCodedRepository {
    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        ANIMES.add(Anime.builder().id(1L).name("Dragon Ball").build());
        ANIMES.add(Anime.builder().id(2L).name("Kimetsu no Yaiba").build());
        ANIMES.add(Anime.builder().id(3L).name("Re:Zero").build());
    }

    public List<Anime> listAll() {
        return ANIMES;
    }

    public List<Anime> findByName(String name) {
        return listAll().stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    public Optional<Anime> findById(Long id) {
        return listAll().stream().filter(anime -> anime.getId().equals(id)).findFirst();
    }

    public Anime save(Anime anime) {
        listAll().add(anime);
        return anime;
    }

    public void delete(Anime animeToDelete) {
        listAll().remove(animeToDelete);
    }

    public void update(Anime animeToUpdate) {
        delete(animeToUpdate);
        save(animeToUpdate);
    }
}
