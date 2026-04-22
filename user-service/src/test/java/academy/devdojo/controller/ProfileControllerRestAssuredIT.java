package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.config.IntegrationTestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestRestTemplate
public class ProfileControllerRestAssuredIT extends IntegrationTestConfig {
    private static final String URL = "/v1/profiles";

    @Autowired
    private FileUtils fileUtils;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        var response = fileUtils.readResourceFile("profile/get-request-profile-list-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

//    @Test
//    @DisplayName("GET v1/profiles returns a empty list when profile is not found")
//    @Order(2)
//    void findAll_ReturnsEmptyList_WhenProfileIsNotFound() {
//        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {
//        };
//
//        var responseEntity = testRestTemplate.exchange(URL, GET, null, typeReference);
//
//        assertThat(responseEntity).isNotNull();
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isNotNull().isEmpty();
//    }
//
//    @Test
//    @DisplayName("POST v1/profiles returns saved profile when successful")
//    @Order(3)
//    void save_ReturnsSavedProfile_WhenSuccessfullySaved() throws Exception {
//        var request = fileUtils.readResourceFile("profile/post_request_profile_200.json");
//
//        var profileRequestEntity = builHttpEntity(request);
//
//        var responseEntity = testRestTemplate.exchange(URL, POST, profileRequestEntity, ProfilePostResponse.class);
//
//        assertThat(responseEntity).isNotNull();
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();
//    }
//
//    @ParameterizedTest
//    @MethodSource("postProfileBadRequestSource")
//    @DisplayName("POST v1/profiles returns bad request when fields are empty or blank and if id is null")
//    @Order(4)
//    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileRequest, String fileResponse) throws Exception {
//        var request = fileUtils.readResourceFile("profile/%s".formatted(fileRequest));
//        var expectedResponse = fileUtils.readResourceFile("profile/%s".formatted(fileResponse));
//
//        var profileRequestEntity = builHttpEntity(request);
//
//        var responseEntity = testRestTemplate.exchange(URL, POST, profileRequestEntity, String.class);
//
//        assertThat(responseEntity).isNotNull();
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//
//        JsonAssertions.assertThatJson(responseEntity.getBody()).whenIgnoringPaths("timestamp").isEqualTo(expectedResponse);
//    }
//
//    private static Stream<Arguments> postProfileBadRequestSource() {
//        return Stream.of(Arguments.of("post-request-profile-empty-fields-400.json", "post-response-profile-empty-fields-400.json"),
//                Arguments.of("post-request-profile-blank-fields-400.json", "post-response-profile-blank-fields-400.json"));
//    }
//
//    private HttpEntity<String> builHttpEntity(String request) {
//        var headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        return new HttpEntity<>(request, headers);
//    }
}

