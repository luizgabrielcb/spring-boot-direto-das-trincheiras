package academy.devdojo.anime;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeMapper mapper;
    private final AnimeService service;

    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        var animes = service.findAll(name);

        var animeGetResponse = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        var anime = service.findByIdOrThrowNotFound(id);

        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest postRequest) {
        var anime = mapper.toAnime(postRequest);

        var animeSaved = service.save(anime);

        var animePostResponse = mapper.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.info("Request to update anime: {}", request);

        var animeToUpdate = mapper.toAnime(request);

        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request to delete anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
