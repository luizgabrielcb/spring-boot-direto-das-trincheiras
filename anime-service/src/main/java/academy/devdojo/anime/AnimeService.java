package academy.devdojo.anime;

import academy.devdojo.domain.Anime;
import academy.devdojo.exception.NameAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository repository;

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByNameIgnoreCase(name);
    }

    public Page<Anime> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Anime not Found"));
    }

    public Anime save(Anime anime) {
        assertAnimeExistsByName(anime.getName());
        return repository.save(anime);
    }

    public void delete(Long id) {
        var animeToDelete = findByIdOrThrowNotFound(id);
        repository.delete(animeToDelete);
    }

    public void update(Anime animeToUpdate) {
        assertAnimeExistsById(animeToUpdate.getId());
        assertAnimeExistsByName(animeToUpdate.getName());
        repository.save(animeToUpdate);
    }

    private void assertAnimeExistsById(Long id) {
        findByIdOrThrowNotFound(id);
    }

    private void assertAnimeExistsByName(String name) {
        var animeList = repository.findByNameIgnoreCase(name);
        if (!animeList.isEmpty()) {
            throw new NameAlreadyExistsException("Anime with name " + name + " already exists");
        }
    }
}
