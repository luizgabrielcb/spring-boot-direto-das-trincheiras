package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.ProfileRepository;
import academy.devdojo.repository.UserProfileRepository;
import academy.devdojo.repository.UserRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class UserControllerTest {
    private static final String URL = "/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository repository;

    @MockitoBean
    private ProfileRepository profileRepository;

    @MockitoBean
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserUtils userUtils;

    private final List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList.addAll(userUtils.newUserList());
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsListWithAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-user-null-first-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/users?name=Satoro returns a list with found user when first name exists")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenNameIsFound() throws Exception {
        var userFound = userList.stream().filter(user -> user.getFirstName().equals("Satoro")).findFirst().orElse(null);
        var firstName = "Satoro";

        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(Collections.singletonList(userFound));

        var response = fileUtils.readResourceFile("user/get-producer-satoro-first-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=x returns a empty list when first name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-x-name-200.json");

        var firstName = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 returns an user given by id")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() throws Exception {
        var id = 1L;
        var userFound = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userFound);

        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/99 throws NotFound 404 when user is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-by-id-404.json");

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/users creates an user")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");

        var userSaved = User.builder().id(99L).firstName("Itachi").lastName("Uchiha").email("itachi@konoha.com").build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userSaved);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/users updates an user")
    @Order(7)
    void update_UpdatesUser_WhenSuccessful() throws Exception {
        var id = 1L;
        var userFound = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userFound);

        var request = fileUtils.readResourceFile("user/put-request-user-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/users throws NotFound when user is not found")
    @Order(8)
    void update_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");
        var response = fileUtils.readResourceFile("user/put-user-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/users/1 removes an user")
    @Order(9)
    void delete_RemoveUser_WhenSuccessful() throws Exception {
        var id = 1L;
        var userFound = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userFound);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users/99 throws NotFound when user is not found")
    @Order(10)
    void delete_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/delete-user-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", 99L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are empty or blank and if email is invalid")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
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
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are empty or blank and if email is invalid ")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(Arguments.of("post-request-user-empty-fields-400.json", userRequiredErrors()),
                Arguments.of("post-request-user-blank-fields-400.json", userRequiredErrors()),
                Arguments.of("post-request-user-invalid-email-400.json", userInvalidErrors()));
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        var userRequiredErrors = userRequiredErrors();
        userRequiredErrors.add("The field 'id' cannot be null");
        var userInvalidErrors = userInvalidErrors();

        return Stream.of(Arguments.of("put-request-user-empty-fields-400.json", userRequiredErrors),
                Arguments.of("put-request-user-blank-fields-400.json", userRequiredErrors),
                Arguments.of("put-request-user-invalid-email-400.json", userInvalidErrors));
    }

    private static List<String> userRequiredErrors() {
        var firstNameRequiredError = "This field 'firstName' is required";
        var lastNameRequiredError = "This field 'lastName' is required";
        var emailRequiredError = "This field 'email' is required";
        return new ArrayList<>(List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError));
    }

    private static List<String> userInvalidErrors() {
        var emailInvalidError = "The e-mail is not valid";
        return List.of(emailInvalidError);
    }
}