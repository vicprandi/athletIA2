package athletia.config.springdoc;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig{

    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        SpringDocConfigProperties config = new SpringDocConfigProperties();
        config.setAutoTagClasses(false);
        return config;
    }
}
