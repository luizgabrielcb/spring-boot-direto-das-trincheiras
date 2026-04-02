package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {
    @InjectMocks
    private AnimeHardCodedRepository repository;
    @Mock
    private AnimeData animeData;
    private final List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        {
            var fullMetalAlchemist = Anime.builder().id(1L).name("Full Metal Alchemist").build();
            var kaguyaSama = Anime.builder().id(2L).name("Kaguya Sama").build();
            var toniKawa = Anime.builder().id(3L).name("Toni Kawa").build();
            animeList.addAll(List.of(fullMetalAlchemist, kaguyaSama, toniKawa));
        }
    }

    @Test
    @DisplayName("findAll returns a list with all animes")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(this.animeList);

        var animes = repository.findAll();

        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(this.animeList);
    }

    @Test
    @DisplayName("findByName return a empty list when name is null")
    @Order(2)
    void findByName_ReturnEmptyList_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(this.animeList);

        var animes = repository.findByName(null);

        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName return a list with animes when name exists")
    @Order(3)
    void findByName_ReturnFoundAnime_WhenNameExists() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(this.animeList);

        var animeExpected = this.animeList.getFirst();

        var animes = repository.findByName(animeExpected.getName());

        Assertions.assertThat(animes).contains(animeExpected);
    }

    @Test
    @DisplayName("findById return an anime when id is found")
    @Order(4)
    void findById_ReturnAnime_WhenIdIsFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(this.animeList);

        var animeExpected = this.animeList.getFirst();

        var animes = repository.findById(animeExpected.getId());

        Assertions.assertThat(animes).isPresent().contains(animeExpected);
    }

    @Test
    @DisplayName("save creates an anime ")
    @Order(5)
    void save_CreatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(this.animeList);

        var animeToSave = Anime.builder().id(99L).name("Drifters").build();

        var animeSaved = repository.save(animeToSave);

        Assertions.assertThat(animeSaved).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeSavedOptional = repository.findById(animeToSave.getId());

        Assertions.assertThat(animeSavedOptional).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete removes an anime")
    @Order(6)
    void delete_RemoveAnime_WhenIsSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(this.animeList);

        var animeToDelete = this.animeList.getFirst();

        repository.delete(animeToDelete);

        Assertions.assertThat(this.animeList).doesNotContain(animeToDelete);
    }

    @Test
    @DisplayName("update updates an anime ")
    @Order(6)
    void update_UpdatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(this.animeList);

        var animeToUpdate = this.animeList.getFirst();
        animeToUpdate.setName("Kimi no Nawa");

        repository.update(animeToUpdate);

        Assertions.assertThat(this.animeList).contains(animeToUpdate);

        var animeUpdatedOptional = repository.findById(animeToUpdate.getId());

        Assertions.assertThat(animeUpdatedOptional).isPresent();
        Assertions.assertThat(animeUpdatedOptional.get().getName()).isEqualTo(animeToUpdate.getName());
    }
}