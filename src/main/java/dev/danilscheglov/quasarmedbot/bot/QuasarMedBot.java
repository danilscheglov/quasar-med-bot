package dev.danilscheglov.quasarmedbot.bot;

import dev.danilscheglov.quasarmedbot.configuration.QuasarMedBotConfiguration;
import dev.danilscheglov.quasarmedbot.service.MessageProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class QuasarMedBot extends TelegramLongPollingBot {

    private final QuasarMedBotConfiguration quasarMedBotConfiguration;
    private final MessageProcessor messageProcessor;

    public QuasarMedBot(QuasarMedBotConfiguration quasarMedBotConfiguration, MessageProcessor messageProcessor) {
        this.quasarMedBotConfiguration = quasarMedBotConfiguration;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public String getBotToken() {
        return quasarMedBotConfiguration.getToken();
    }

    @Override
    public String getBotUsername() {
        return quasarMedBotConfiguration.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        try {
            List<String> responses = messageProcessor.processMessage(chatId, text);
            if (responses != null) {
                for (String response : responses) {
                    sendMessage(chatId, response, true);
                }
            }
        } catch (Exception e) {
            sendMessage(chatId, "❗ Произошла ошибка\\. Пожалуйста, попробуйте снова\\.", true);
        }
    }

    private void sendMessage(long chatId, String text, boolean enableMarkdown) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);
            if (enableMarkdown) {
                message.setParseMode("MarkdownV2");
            }
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
