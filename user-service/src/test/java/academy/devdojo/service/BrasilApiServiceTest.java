package academy.devdojo.service;

import academy.devdojo.commons.CepUtils;
import academy.devdojo.config.BrasilApiConfigurationProperties;
import academy.devdojo.config.RestClientConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;

@RestClientTest({BrasilApiService.class,
        RestClientConfiguration.class,
        BrasilApiConfigurationProperties.class,
        ObjectMapper.class,
        CepUtils.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrasilApiServiceTest {
    @Autowired
    private BrasilApiService service;

    @Autowired
    private RestClient.Builder brasilApiRestClientBuilder;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private BrasilApiConfigurationProperties properties;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CepUtils cepUtils;

    @AfterEach
    void reset() {
        server.reset();
    }

    @Test
    @DisplayName("findCep returns CepGetResponse when successful")
    @Order(1)
    void findCep_ReturnsCepGetResponse_WhenSuccessful() throws JsonProcessingException {
        server = MockRestServiceServer.bindTo(brasilApiRestClientBuilder).build();

        var cep = "00000000";
        var cepGetResponse = cepUtils.newCepGetResponse();
        var jsonResponse = mapper.writeValueAsString(cepGetResponse);
        var requestTo = MockRestRequestMatchers.requestToUriTemplate(properties.baseUrl() + properties.cepUri(), cep);
        var withSuccess = MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON);
        server.expect(requestTo).andRespond(withSuccess);

        Assertions.assertThat(service.findCep(cep))
                .isNotNull()
                .isEqualTo(cepGetResponse);
    }
}