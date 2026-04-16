package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.repository.UserRepository;
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
class UserServiceTest {
    @InjectMocks
    private UserService service;

    @InjectMocks
    private UserUtils userUtils;

    @Mock
    private UserRepository repository;

    private final List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList.addAll(userUtils.newUserList());
    }

    @Test
    @DisplayName("findAll returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsListWithAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll returns a list with found user when first name exists")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenFirstNameIsFound() {
        var user = userList.getFirst();
        var expectedUserInList = Collections.singletonList(user);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName())).thenReturn(expectedUserInList);

        var usersFound = service.findAll(user.getFirstName());

        Assertions.assertThat(usersFound).containsAll(expectedUserInList);
    }

    @Test
    @DisplayName("findByName returns a empty list when first name is not found")
    @Order(3)
    void findByFirstName_ReturnsEmptyList_WhenFirstNameIsNotFound() {
        var firstName = "not-found";

        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(Collections.emptyList());

        var users = repository.findByFirstNameIgnoreCase(firstName);

        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns an user given by id")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() {
        var userExpected = userList.getFirst();

        BDDMockito.when(repository.findById(userExpected.getId())).thenReturn(Optional.of(userExpected));

        var userFound = service.findByIdOrThrowNotFound(userExpected.getId());

        Assertions.assertThat(userFound).isEqualTo(userExpected);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException 404 when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userExpected = userList.getFirst();
        BDDMockito.when(repository.findById(userExpected.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(userExpected.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = User.builder().id(99L).firstName("Naruto").lastName("Uzumaki").email("narutin@hokage.folha.br").build();

        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);
        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.empty());

        var savedUser = service.save(userToSave);

        Assertions.assertThat(savedUser).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes an user")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when user is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates an user")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {
        var userToUpdate = userList.getFirst().withFirstName("Itachi");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when user is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToUpdate = userList.getFirst();

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save throws EmailAlreadyExistsException when email already exists")
    @Order(11)
    void save_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        var userToSave = userList.getFirst();
        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.of(userToSave));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("update throws EmailAlreadyExistsException when email belongs to another user")
    @Order(12)
    void update_ThrowsEmailAlreadyExistsException_WhenEmailBelongsToAnotherUser() {
        var savedUser = userList.getFirst();
        var userToUpdate = User.builder().id(99999L).firstName("Cristiano").lastName("Ronaldo").email(savedUser.getEmail()).build();

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(userToUpdate.getEmail(), userToUpdate.getId())).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }
}