package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.domain.Profile;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
@WithMockUser
class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileUtils profileUtils;

    @Autowired
    private FileUtils fileUtils;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ProfileRepository repository;

    @MockitoBean
    private UserProfileRepository userProfileRepository;

    private static final String URL = "/v1/profiles";

    private final List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void init() {
        profileList.addAll(profileUtils.newProfileList());
    }

    @Test
    @DisplayName("POST v1/profiles returns saved profile when successful")
    @Order(1)
    void save_ReturnsSavedProfile_WhenSuccessfullySaved() throws Exception {
        var profileToSave = Profile.builder().id(1L).name("Regular User").description("Regular user with regular permissions").build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileToSave);

        var request = fileUtils.readResourceFile("profile/post_request_profile_200.json");
        var response = fileUtils.readResourceFile("profile/post_response_profile_201.json");

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profiles returns bad request when fields are empty or blank and if id is null")
    @Order(2)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

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

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Order(3)
    void findAll_ReturnsListWithAllProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var response = fileUtils.readResourceFile("profile/get-response-profile-list-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    private static Stream<Arguments> postProfileBadRequestSource() {
        return Stream.of(Arguments.of("post-request-profile-empty-fields-400.json", profileErrors()),
                Arguments.of("post-request-profile-blank-fields-400.json", profileErrors()));
    }

    private static List<String> profileErrors() {
        var nameRequiredError = "This field 'name' is required";
        var descriptionRequiredError = "This field 'description' is required";
        return new ArrayList<>(List.of(nameRequiredError, descriptionRequiredError));
    }
}