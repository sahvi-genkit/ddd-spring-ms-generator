package <%= base_package_name %>.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "rest.client")
public class RestClientProperties {
    private String baseUrl;
    private int connectTimeout = 5000;
    private int readTimeout = 5000;
    private int maxTotalConnections = 100;
    private int maxPerRouteConnections = 20;
    private boolean enableRetry = true;
    private int maxRetries = 3;
    private int retryDelay = 1000;
} 