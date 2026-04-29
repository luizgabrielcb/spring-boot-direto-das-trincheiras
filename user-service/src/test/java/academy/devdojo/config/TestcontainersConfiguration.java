package academy.devdojo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@Profile("itest")
public class TestcontainersConfiguration {
    private static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer(DockerImageName.parse("mysql:9.6.0"))
                .withDatabaseName("user_service");
        MY_SQL_CONTAINER.start();
    }

    @Bean
    @ServiceConnection
    MySQLContainer mysqlContainer() {
        return MY_SQL_CONTAINER;
    }
}