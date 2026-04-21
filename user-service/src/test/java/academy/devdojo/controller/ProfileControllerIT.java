package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestRestTemplate
public class ProfileControllerIT {
    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FileUtils fileUtils;

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {
        };

        var responseEntity = testRestTemplate.exchange(URL, GET, null, typeReference);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().doesNotContainNull();

        responseEntity
                .getBody()
                .forEach(profileGetResponse -> assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("GET v1/profiles returns a empty list when profile is not found")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenProfileIsNotFound() {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {
        };

        var responseEntity = testRestTemplate.exchange(URL, GET, null, typeReference);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("POST v1/profiles returns saved profile when successful")
    @Sql(value = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(3)
    void save_ReturnsSavedProfile_WhenSuccessfullySaved() throws Exception {
        var request = fileUtils.readResourceFile("profile/post_request_profile_200.json");

        var profileRequestEntity = builHttpEntity(request);

        var responseEntity = testRestTemplate.exchange(URL, POST, profileRequestEntity, ProfilePostResponse.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

    private HttpEntity<String> builHttpEntity(String request) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(request, headers);
    }
}

