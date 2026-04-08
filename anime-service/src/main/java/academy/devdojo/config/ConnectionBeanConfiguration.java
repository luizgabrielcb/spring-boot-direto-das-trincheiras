package academy.devdojo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
    @Profile("test")
    public Connection connectionTest() {
        return new Connection(configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }

    @Bean
    @Profile("mysql")
    public Connection connectionMySql() {
        return new Connection(configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }

    @Bean
    @Profile("mongo")
    public Connection connectionMongoDb() {
        return new Connection(configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }
}
