package academy.devdojo.controller;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.commons.FileUtils;
import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan("academy.devdojo")
class AnimeControllerTest {
    private static final String URL = "/v1/animes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimeData animeData;

    @MockitoSpyBean
    private AnimeHardCodedRepository repository;

    @Autowired
    private AnimeUtils animeUtils;

    @Autowired
    private FileUtils fileUtils;

    private final List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        animeList.addAll(animeUtils.newAnimeList());
    }

    @Test
    @DisplayName("GET v1/animes returns a list with all animes when name is null")
    @Order(1)
    void findAll_ReturnsFindWithAllAnimes_WhenNameIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var fileResource = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
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

        var fileResource = fileUtils.readResourceFile("anime/get-anime-jujutsu-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", animeExpected.getName()))
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

        var fileResource = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", anime.getName()))
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

        var fileResource = fileUtils.readResourceFile("anime/get-anime-jujutsu-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
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

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
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

        var request = fileUtils.readResourceFile("anime/post-anime-request-200.json");
        var response = fileUtils.readResourceFile("anime/post-anime-response-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
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

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", animeToDelete.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("DELETE v1/animes/999 throws ResponseStatusException 404 when id is not found")
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var id = 999L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }

    @Test
    @Order(9)
    @DisplayName("PUT v1/animes updates an anime when successful")
    void update_UpdatesAnime_WhenIdSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
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
        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not Found"));
    }
}