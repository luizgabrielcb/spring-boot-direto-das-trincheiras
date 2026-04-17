package academy.devdojo.anime;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.domain.Anime;
import academy.devdojo.exception.NameAlreadyExistsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;

    @InjectMocks
    private AnimeUtils animeUtils;

    @Mock
    private AnimeRepository repository;

    private final List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        animeList.addAll(animeUtils.newAnimeList());
    }

    @Test
    @DisplayName("findAll returns a list with all animes when argument is null")
    @Order(1)
    void findAll_ReturnsFindWithAllAnimes_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(this.animeList);

        var animeListExpected = service.findAll(null);

        Assertions.assertThat(animeListExpected).isNotNull().hasSameElementsAs(this.animeList);
    }

    @Test
    @DisplayName("findAll returns found anime when name exists")
    @Order(2)
    void findAll_ReturnsFoundAnime_WhenNameExists() {
        var anime = animeList.getFirst();
        var animesFoundExpected = Collections.singletonList(anime);

        BDDMockito.when(repository.findByNameIgnoreCase(anime.getName())).thenReturn(animesFoundExpected);

        var animesFound = service.findAll(anime.getName());

        Assertions.assertThat(animesFound).containsAll(animesFoundExpected);
    }

    @Test
    @DisplayName("findAll returns a empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyFind_WhenNameIsNotFound() {
        var name = "not-found";

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(Collections.emptyList());

        var animeList = service.findAll(name);

        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns found anime when id exists")
    @Order(4)
    void findById_ReturnsAnimeById_WhenIdExists() {
        var animeToFound = animeList.getFirst();

        BDDMockito.when(repository.findById(animeToFound.getId())).thenReturn(Optional.of(animeToFound));

        Anime animeFound = service.findByIdOrThrowNotFound(animeToFound.getId());

        Assertions.assertThat(animeFound).isEqualTo(animeToFound);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var anime = Anime.builder().id(99L).name("not-found").build();

        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(anime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates an anime when successful")
    @Order(6)
    void save_CreatesAnime_WhenSuccessful() {
        var animeToCreate = animeList.getFirst();

        BDDMockito.when(repository.findByNameIgnoreCase(animeToCreate.getName())).thenReturn(Collections.emptyList());
        BDDMockito.when(repository.save(animeToCreate)).thenReturn(animeToCreate);

        var animeCreated = service.save(animeToCreate);

        Assertions.assertThat(animeCreated).isEqualTo(animeToCreate);
    }

    @Test
    @DisplayName("delete removes an anime when successful")
    @Order(7)
    void delete_RemovesAnime_WhenSuccessful() {
        var animeToDelete = animeList.getFirst();

        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToDelete = Anime.builder().id(99L).name("not-found").build();

        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates an anime when successful")
    @Order(9)
    void update_UpdatesAnime_WhenSuccessful() {
        var animeToUpdate = animeList.getFirst();

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));

        BDDMockito.when(repository.findByNameIgnoreCase(animeToUpdate.getName())).thenReturn(Collections.emptyList());

        BDDMockito.when(repository.save(animeToUpdate)).thenReturn(animeToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException 404 when anime is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToUpdate = Anime.builder().id(99L).name("not-found").build();

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save throws NameAlreadyExistsException when name already exists")
    @Order(11)
    void save_ThrowsNameAlreadyExistsException_WhenAnimeNameAlreadyExists() {
        var animeToCreate = animeList.getFirst();

        BDDMockito.when(repository.findByNameIgnoreCase(animeToCreate.getName())).thenReturn(Collections.singletonList(animeToCreate));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(animeToCreate))
                .isInstanceOf(NameAlreadyExistsException.class);
    }

    @Test
    @DisplayName("update throws NameAlreadyExistsException when name already exists")
    @Order(12)
    void update_ThrowsNameAlreadyExistsException_WhenAnimeNameAlreadyExists() {
        var animeToUpdate = animeList.getFirst();

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.when(repository.findByNameIgnoreCase(animeToUpdate.getName())).thenReturn(Collections.singletonList(animeToUpdate));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(NameAlreadyExistsException.class);
    }
}