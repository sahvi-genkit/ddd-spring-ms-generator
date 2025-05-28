package <%= base_package_name %>.infrastructure.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
@EnableRetry
public class RestClientConfig {

    private final RestClientProperties properties;

    public RestClientConfig(RestClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .requestFactory(this::clientHttpRequestFactory)
            .setConnectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
            .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
            .build();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        return factory;
    }
} 