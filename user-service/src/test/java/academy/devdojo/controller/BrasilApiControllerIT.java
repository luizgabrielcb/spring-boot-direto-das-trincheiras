package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.config.RestAssuredConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/sql/init_one_login_regular_user.sql")
@Sql(value = "/sql/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@EnableWireMock({
        @ConfigureWireMock(
                port = 0,
                filesUnderClasspath = "wiremock/brasil-api/cep"
        )
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(RestAssuredConfig.class)
public class BrasilApiControllerIT extends IntegrationTestConfig {
    private static final String URL = "v1/brasil-api/cep";

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    @Qualifier(value = "requestSpecificationRegularUser")
    private RequestSpecification requestSpecificationRegularUser;

    @BeforeEach
    void setup() {
        RestAssured.requestSpecification = requestSpecificationRegularUser;
    }

    @Test
    @DisplayName("findCep returns CepGetResponse when successful")
    @Order(1)
    void findCep_ReturnsCepGetResponse_WhenSuccessful() {
        var cep = "00000000";
        var expectedResponse = fileUtils.readResourceFile("brasil-api/cep/expected-get-cep-response-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL + "/{cep}", cep)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("findCep returns CepErrorResponse when successful")
    @Order(2)
    void findCep_ReturnsCepErrorResponse_WhenSuccessful() {
        var cep = "40400000";
        var expectedResponse = fileUtils.readResourceFile("brasil-api/cep/expected-get-cep-response-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL + "/{cep}", cep)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

}
