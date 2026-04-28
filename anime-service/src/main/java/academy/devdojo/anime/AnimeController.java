package academy.devdojo.anime;

import academy.devdojo.api.AnimeControllerApi;
import academy.devdojo.dto.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class AnimeController implements AnimeControllerApi {
    private final AnimeMapper mapper;
    private final AnimeService service;

    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> findAllAnimes(@RequestParam(required = false) String name) {
        var animes = service.findAll(name);

        var animeGetResponse = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("/paginated")
    public ResponseEntity<PageAnimeGetResponse> findAllAnimesPaginated(@ParameterObject Pageable pageable) {
        var jpaPageAnimeGetResponse = service.findAllPaginated(pageable);

        var pageAnimeGetResponse = mapper.toPageAnimeGetResponse(jpaPageAnimeGetResponse);

        return ResponseEntity.ok(pageAnimeGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findAnimeById(@PathVariable Long id) {
        var anime = service.findByIdOrThrowNotFound(id);

        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> saveAnime(@RequestBody @Valid AnimePostRequest postRequest) {
        var anime = mapper.toAnime(postRequest);

        var animeSaved = service.save(anime);

        var animePostResponse = mapper.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateAnime(@RequestBody @Valid AnimePutRequest request) {
        log.info("Request to update anime: {}", request);

        var animeToUpdate = mapper.toAnime(request);

        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAnime(@PathVariable Long id) {
        log.info("Request to delete anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
