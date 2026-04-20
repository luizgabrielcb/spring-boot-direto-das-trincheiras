package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({UserUtils.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileRepositoryTest {
    @Autowired
    private UserProfileRepository repository;

    @Test
    @DisplayName("findAllUsersByProfileId returns a list with all users by profile id")
    @Order(1)
    @Sql("/sql/init_user_profile_2_users_1_profile.sql")
    void findAllUsersByProfileId_ReturnsListWithAllUsersByProfileId_WhenSuccessful() {
        var profileId = 1L;
        var users = repository.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users).isNotEmpty().hasSize(2);

        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }
}