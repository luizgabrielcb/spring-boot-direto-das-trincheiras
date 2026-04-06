package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan("academy.devdojo")
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimeData animeData;

    @MockitoSpyBean
    private AnimeHardCodedRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;

    private final List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        var jujutsuKaisen = Anime.builder().id(1L).name("Jujutsu Kaisen").build();
        var bokuNoHero = Anime.builder().id(2L).name("Boku no Hero").build();
        var onePiece = Anime.builder().id(3L).name("One Piece").build();
        animeList.addAll(List.of(jujutsuKaisen, bokuNoHero, onePiece));
    }

    @Test
    @DisplayName("GET v1/animes returns a list with all animes when name is null")
    @Order(1)
    void findAll_ReturnsFindWithAllAnimes_WhenNameIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var fileResource = readFileResource("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(fileResource));
    }

    @Test
    @Order(2)
    @DisplayName("GET v1/animes?name=jujutsu kaisen returns a list with anime when name exists")
    void findAll_ReturnsAnimeByName_WhenNameExists() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeExpected = animeList.getFirst();

        var fileResource = readFileResource("anime/get-anime-jujutsu-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", animeExpected.getName()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(fileResource));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/animes?name=x returns a empty list when anime is not found")
    void findAll_ReturnsEmptyFind_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var anime = Anime.builder().id(99L).name("xxx").build();

        var fileResource = readFileResource("anime/get-anime-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", anime.getName()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(fileResource));
    }

    @Test
    @Order(4)
    @DisplayName("GET v1/animes/1 returns an anime with given by id")
    void findById_ReturnsAnimeById_WhenIdExists() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var id = animeList.getFirst().getId();

        var fileResource = readFileResource("anime/get-anime-jujutsu-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(fileResource));
    }

    @Test
    @Order(5)
    @DisplayName("GET v1/animes/999 throws ResponseStatusException 404 when id is not found")
    void findById_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var id = 999L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    @Test
    @Order(6)
    @DisplayName("POST v1/animes creates an anime")
    void save_CreatesAnime_WhenSuccessful() throws Exception {
        var animeToSave = Anime.builder().id(99L).name("Death Note").build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        var request = readFileResource("anime/post-anime-request-200.json");
        var response = readFileResource("anime/post-anime-response-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(7)
    @DisplayName("DELETE v1/animes/1 removes an anime")
    void delete_RemovesAnime_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToDelete = animeList.getFirst();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", animeToDelete.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("DELETE v1/animes/999 throws ResponseStatusException 404 when id is not found")
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var id = 999L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    @Test
    @Order(9)
    @DisplayName("PUT v1/animes updates an anime when successful")
    void update_UpdatesAnime_WhenIdSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var request = readFileResource("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("PUT v1/animes throws ResponseStatusException 404 when id is not found")
    void update_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var request = readFileResource("anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    private String readFileResource(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}