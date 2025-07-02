package dev.danilscheglov.quasarmedbot.service;

import dev.danilscheglov.quasarmedbot.model.UserData;
import dev.danilscheglov.quasarmedbot.model.UserState;
import dev.danilscheglov.quasarmedbot.model.enums.State;
import dev.danilscheglov.quasarmedbot.util.BotMessages;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageProcessor {

    private static final String NAME_PATTERN = "^[a-zA-Zа-яА-Я\\s]+$";
    private static final String DATE_PATTERN = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
    private static final String PRESSURE_PATTERN = "^\\d+/\\d+$";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final Map<Long, UserState> userStates = new ConcurrentHashMap<>();
    private final Map<Long, UserData> userData = new ConcurrentHashMap<>();

    private String escapeMarkdown(String text) {
        return text.replace(".", "\\.").replace("-", "\\-").replace("!", "\\!");
    }

    public List<String> processMessage(long chatId, String text) {
        if (text.startsWith("/")) {
            String response = processCommand(chatId, text);
            return Arrays.asList(response, BotMessages.REQUEST_NAME);
        }

        UserState state = userStates.computeIfAbsent(chatId, id -> new UserState(State.AWAITING_NAME));

        return switch (state.getState()) {
            case AWAITING_NAME -> processName(chatId, text, state);
            case AWAITING_BIRTHDATE -> processBirthdate(chatId, text, state);
            case AWAITING_PRESSURE -> processPressureReading(text);
        };
    }

    private String processCommand(long chatId, String command) {
        if (command.equalsIgnoreCase("/start")) {
            userStates.put(chatId, new UserState(State.AWAITING_NAME));
            userData.remove(chatId);
            return BotMessages.START_MESSAGE;
        }
        return BotMessages.UNKNOWN_COMMAND;
    }

    private List<String> processName(long chatId, String text, UserState state) {
        if (!text.matches(NAME_PATTERN)) {
            return Collections.singletonList(BotMessages.REQUEST_NAME);
        }

        state.setName(text);
        state.setState(State.AWAITING_BIRTHDATE);
        userStates.put(chatId, state);
        return Arrays.asList("✅ *ФИО записано*: " + escapeMarkdown(text), BotMessages.REQUEST_BIRTHDATE);
    }

    private List<String> processBirthdate(long chatId, String text, UserState state) {
        if (!text.matches(DATE_PATTERN)) {
            return Collections.singletonList(BotMessages.REQUEST_BIRTHDATE);
        }

        try {
            LocalDate birthdate = LocalDate.parse(text, DATE_FORMATTER);
            LocalDate today = LocalDate.now();

            if (birthdate.isAfter(today) || birthdate.isBefore(today.minusYears(100))) {
                return Collections.singletonList(BotMessages.INVALID_BIRTHDATE_RANGE);
            }

            userData.put(chatId, new UserData(state.getName(), birthdate));
            state.setState(State.AWAITING_PRESSURE);
            userStates.put(chatId, state);
            return Arrays.asList("✅ *Дата рождения записана*: " + escapeMarkdown(text), BotMessages.REQUEST_PRESSURE);
        } catch (DateTimeParseException e) {
            return Collections.singletonList(BotMessages.INVALID_BIRTHDATE_FORMAT);
        }
    }

    private List<String> processPressureReading(String text) {
        if (!text.matches(PRESSURE_PATTERN)) {
            return Collections.singletonList(BotMessages.INVALID_PRESSURE_FORMAT);
        }

        try {
            String[] parts = text.split("/");
            int systolic = Integer.parseInt(parts[0]);
            int diastolic = Integer.parseInt(parts[1]);

            if (systolic < 90 || systolic > 200 || diastolic < 60 || diastolic > 120) {
                return Collections.singletonList(BotMessages.PRESSURE_OUT_OF_RANGE);
            }

            return Collections.singletonList("✅ *Показания давления записаны*: " + escapeMarkdown(text));
        } catch (NumberFormatException e) {
            return Collections.singletonList(BotMessages.PRESSURE_PARSE_ERROR);
        }
    }
}
