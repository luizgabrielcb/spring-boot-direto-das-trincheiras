package academy.devdojo.controller;

import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import academy.devdojo.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;
    private AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }

    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        var animes = service.listAll(name);

        var animeGetResponse = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        var anime = service.findByIdOrThrowAnimeNotFound(id);

        var animeGetResponse = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest postRequest) {
        var anime = MAPPER.toAnime(postRequest);

        var animeSaved = service.save(anime);

        var animePostResponse = MAPPER.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request to delete anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {
        log.info("Request to update anime: {}", request);

        var animeToUpdate = MAPPER.toAnime(request);

        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }
}
