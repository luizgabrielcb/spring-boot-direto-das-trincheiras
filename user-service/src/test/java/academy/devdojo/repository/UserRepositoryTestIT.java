package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.config.IntegrationTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(UserUtils.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTestIT extends IntegrationTestConfig {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("save creates an user")
    @Order(1)
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSave();
        var savedUser = repository.save(userToSave);

        Assertions.assertThat(savedUser).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("findAll returns a list with all users")
    @Order(2)
    @Sql("/sql/init_one_user.sql")
    void findAll_ReturnsListWithAllUsers_WhenSuccessful() {
        var users = repository.findAll();

        Assertions.assertThat(users).isNotEmpty().hasSize(1);
    }
}