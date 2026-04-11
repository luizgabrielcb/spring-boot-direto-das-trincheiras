package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
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
class UserHardCodedRepositoryTest {
    @InjectMocks
    private UserHardCodedRepository repository;

    @Mock
    private UserData userData;

    @InjectMocks
    private UserUtils userUtils;

    private final List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList.addAll(userUtils.newUserList());
    }

    @Test
    @DisplayName("findAll returns a list with all users")
    @Order(1)
    void findAll_ReturnsListWithAllUsers_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findById returns an user given by id")
    @Order(2)
    void findById_ReturnsUserById_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userExpected = userList.getFirst();

        var user = repository.findById(userExpected.getId());

        Assertions.assertThat(user).isPresent().contains(userExpected);
    }

    @Test
    @DisplayName("findByFirstName returns a empty list when first name is null")
    @Order(3)
    void findByFirstName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var user = repository.findByFirstName(null);

        Assertions.assertThat(user).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByFirstName returns a list with found object when name exists")
    @Order(4)
    void findByFirstName_ReturnsFoundUserInList_WhenNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userExpected = userList.getFirst();

        var user = repository.findByFirstName(userExpected.getFirstName());

        Assertions.assertThat(user).isNotNull().contains(userExpected);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(5)
    void save_CreatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToSave = User.builder().id(99L).firstName("Tanjiro").lastName("Kamado").email("tanjiro@mail.com").build();
        var userSaved = repository.save(userToSave);

        Assertions.assertThat(userSaved).isEqualTo(userToSave).hasNoNullFieldsOrProperties();

        var userSavedOptional = repository.findById(userToSave.getId());

        Assertions.assertThat(userSavedOptional).isPresent().contains(userToSave);
    }

    @Test
    @DisplayName("delete removes an user")
    @Order(6)
    void delete_RemovesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToDelete = userList.getFirst();

        repository.delete(userToDelete);

        Assertions.assertThat(userList).doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates an user")
    @Order(7)
    void update_UpdatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("Mahoraga");

        repository.update(userToUpdate);

        Assertions.assertThat(userList).contains(userToUpdate);

        var userUpdatedOptional = repository.findById(userToUpdate.getId());

        Assertions.assertThat(userUpdatedOptional).isPresent();
        Assertions.assertThat(userUpdatedOptional.get().getFirstName()).isEqualTo(userToUpdate.getFirstName());
    }
}