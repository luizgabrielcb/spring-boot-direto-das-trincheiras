package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimeHardCodedRepository {
    private final AnimeData animeData;

    public List<Anime> findAll() {
        return animeData.getAnimes();
    }

    public List<Anime> findByName(String name) {
        return findAll().stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    public Optional<Anime> findById(Long id) {
        return findAll().stream().filter(anime -> anime.getId().equals(id)).findFirst();
    }

    public Anime save(Anime anime) {
        findAll().add(anime);
        return anime;
    }

    public void delete(Anime animeToDelete) {
        findAll().remove(animeToDelete);
    }

    public void update(Anime animeToUpdate) {
        delete(animeToUpdate);
        save(animeToUpdate);
    }
}
