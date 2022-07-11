package ru.senla.autoservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.nio.file.Paths;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {

    public static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString() +
            "\\autoserviceAdmin\\src\\main\\java\\ru\\senla\\autoservice";

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer pspc
                = new PropertySourcesPlaceholderConfigurer();
        pspc.setIgnoreUnresolvablePlaceholders(true);

        return pspc;
    }

}
