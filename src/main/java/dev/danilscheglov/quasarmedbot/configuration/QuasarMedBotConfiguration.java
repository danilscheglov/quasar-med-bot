package dev.danilscheglov.quasarmedbot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
public record QuasarMedBotConfiguration(String token, String username) {
}
