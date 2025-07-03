package dev.danilscheglov.quasarmedbot;

import dev.danilscheglov.quasarmedbot.configuration.QuasarMedBotConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(QuasarMedBotConfiguration.class)
public class QuasarMedBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuasarMedBotApplication.class, args);
    }

}
