package academy.devdojo.service;

import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.domain.Profile;
import academy.devdojo.repository.ProfileRepository;
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
class ProfileServiceTest {
    @InjectMocks
    private ProfileService service;

    @InjectMocks
    private ProfileUtils utils;

    @Mock
    private ProfileRepository repository;

    private final List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void init() {
        profileList.addAll(utils.newProfileList());
    }

    @Test
    @DisplayName("save returns saved profile when successful")
    @Order(1)
    void save_ReturnsSavedProfile_WhenSuccessfullySaved() {
        var profileToSave = Profile.builder().name("Regular User").description("Regular user with regular permissions").build();

        BDDMockito.when(repository.save(profileToSave)).thenReturn(profileToSave);

        var profileSaved = service.save(profileToSave);

        Assertions.assertThat(profileSaved).isEqualTo(profileToSave);
    }

    @Test
    @DisplayName("findAll returns a list with all profiles")
    @Order(2)
    void findAll_ReturnsListWithAllProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var profileListExpected = service.findAll();

        Assertions.assertThat(profileListExpected).isNotNull().hasSameElementsAs(profileList);
    }
}