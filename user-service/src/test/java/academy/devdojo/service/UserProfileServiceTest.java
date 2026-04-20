package academy.devdojo.service;

import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.commons.UserProfileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.UserProfile;
import academy.devdojo.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileServiceTest {
    @InjectMocks
    private UserProfileService service;

    @InjectMocks
    private UserProfileUtils userProfileUtils;

    @Mock
    private UserProfileRepository repository;

    @Spy
    private UserUtils userUtils;

    @Spy
    private ProfileUtils profileUtils;

    private final List<UserProfile> userProfileList = new ArrayList<>();

    @BeforeEach
    void init() {
        userProfileList.addAll(userProfileUtils.newUserProfileList());
    }

    @Test
    @DisplayName("findAll returns a list with all user profiles")
    @Order(1)
    void findAll_ReturnsAllUserProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var userProfiles = service.findAll();

        Assertions.assertThat(userProfiles).isNotNull().hasSameElementsAs(userProfileList);

        userProfiles.forEach(userProfile -> Assertions.assertThat(userProfile).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("findAllUsersByProfileId returns all users by profile id")
    @Order(2)
    void findAllUsersByProfileId_ReturnsAllUsersByProfileId_WhenSuccessful() {
        var profileId = 99L;
        var userByProfile = userProfileList.stream()
                .filter(userProfile -> userProfile.getProfile().getId().equals(profileId))
                .map(UserProfile::getUser)
                .toList();

        BDDMockito.when(repository.findAllUsersByProfileId(profileId)).thenReturn(userByProfile);

        var users = service.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userByProfile);

        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }
}