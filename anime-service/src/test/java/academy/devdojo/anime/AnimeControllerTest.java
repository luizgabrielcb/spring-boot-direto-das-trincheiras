package academy.devdojo.anime;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.commons.FileUtils;
import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo.anime", "academy.devdojo.commons", "academy.devdojo.config"})
@WithMockUser
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimeRepository repository;

    @Autowired
    private AnimeUtils animeUtils;

    @Autowired
    private FileUtils fileUtils;

    private final List<Anime> animeList = new ArrayList<>();

    private static final String URL = "/v1/animes";

    @BeforeEach
    void init() {
        animeList.addAll(animeUtils.newAnimeList());
    }

    @Test
    @DisplayName("GET v1/animes returns 403 when role is not USER")
    @Order(1)
    @WithMockUser(roles = "ADMIN")
    void findAll_Returns403_WhenRoleIsNotUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("GET v1/animes returns a list with all animes when name is null")
    @Order(1)
    void findAll_ReturnsFindWithAllAnimes_WhenNameIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/animes returns a paginated list of animes")
    @Order(1)
    void findAllPaginated_ReturnsPaginatedAnimeList_WhenSuccessful() throws Exception {
        var pageRequest = PageRequest.of(0, animeList.size());
        var pageAnime = new PageImpl<>(animeList, pageRequest, 1);

        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(pageAnime);

        var response = fileUtils.readResourceFile("anime/get-anime-paginated-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/paginated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(2)
    @DisplayName("GET v1/animes?name=jujutsu kaisen returns a list with anime when name exists")
    void findAll_ReturnsAnimeByName_WhenNameExists() throws Exception {
        var animeExpected = animeList.getFirst();

        BDDMockito.when(repository.findByNameIgnoreCase(animeExpected.getName())).thenReturn(Collections.singletonList(animeExpected));

        var response = fileUtils.readResourceFile("anime/get-anime-jujutsu-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", animeExpected.getName()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/animes?name=x returns a empty list when anime is not found")
    void findAll_ReturnsEmptyFind_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(Collections.emptyList());

        var anime = Anime.builder().id(9999L).name("xxx").build();

        var response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", anime.getName()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("GET v1/animes/1 returns an anime with given by id")
    void findById_ReturnsAnimeById_WhenIdExists() throws Exception {
        var anime = animeList.getFirst();
        var id = anime.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(anime));

        var response = fileUtils.readResourceFile("anime/get-anime-jujutsu-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(5)
    @DisplayName("GET v1/animes/999 throws NotFound 404 when id is not found")
    void findById_ThrowsNotFound_WhenIdIsNotFound() throws Exception {
        var id = 999L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        var response = fileUtils.readResourceFile("anime/get-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
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
        var animeToDelete = animeList.getFirst();
        var id = animeToDelete.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(animeToDelete));


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("DELETE v1/animes/999 throws NotFound 404 when id is not found")
    void delete_ThrowsNotFound_WhenIdIsNotFound() throws Exception {
        var id = 999L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        var response = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(9)
    @DisplayName("PUT v1/animes updates an anime when successful")
    void update_UpdatesAnime_WhenIdSuccessful() throws Exception {
        var anime = animeList.getFirst();

        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));

        var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("PUT v1/animes throws NotFound 404 when id is not found")
    void update_ThrowsNotFound_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(repository.findById(99999L)).thenReturn(Optional.empty());

        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");
        var response = fileUtils.readResourceFile("anime/put-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @Order(11)
    @DisplayName("POST v1/animes returns bad request when fields are invalid")
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putAnimeBadRequestSource")
    @Order(12)
    @DisplayName("PUT v1/animes returns bad request when fields are invalid")
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postAnimeBadRequestSource() {
        var animeErrors = animeErrors();
        animeErrors.remove("The field 'id' cannot be null");
        return Stream.of(Arguments.of("post-anime-request-empty-fields-400.json", animeErrors),
                Arguments.of("post-anime-request-blank-fields-400.json", animeErrors));
    }

    private static Stream<Arguments> putAnimeBadRequestSource() {
        return Stream.of(Arguments.of("put-anime-request-blank-fields-400.json", animeErrors()),
                Arguments.of("put-anime-request-empty-fields-400.json", animeErrors()),
                Arguments.of("put-anime-request-null-fields-400.json", animeErrors()));
    }

    private static List<String> animeErrors() {
        var idNullError = "The field 'id' cannot be null";
        var nameRequiredError = "The field 'name' is required";
        return new ArrayList<>(List.of(idNullError, nameRequiredError));
    }
}

