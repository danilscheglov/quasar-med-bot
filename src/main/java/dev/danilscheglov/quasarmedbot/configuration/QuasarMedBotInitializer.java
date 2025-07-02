package dev.danilscheglov.quasarmedbot.configuration;

import dev.danilscheglov.quasarmedbot.bot.QuasarMedBot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class QuasarMedBotInitializer  implements CommandLineRunner {

    private final QuasarMedBot quasarMedBot;

    public QuasarMedBotInitializer(QuasarMedBot quasarMedBot) {
        this.quasarMedBot = quasarMedBot;
    }

    @Override
    public void run(String... args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(quasarMedBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to register bot: " + e.getMessage(), e);
        }
    }

}
