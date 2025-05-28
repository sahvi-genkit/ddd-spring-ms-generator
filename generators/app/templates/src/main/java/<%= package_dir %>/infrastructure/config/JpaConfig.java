package <%= base_package_name %>.infrastructure.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA configuration class.
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
// @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class JpaConfig {
    // Add custom JPA configuration here if needed
}
