package dev.danilscheglov.quasarmedbot.service;

import dev.danilscheglov.quasarmedbot.model.UserData;
import dev.danilscheglov.quasarmedbot.model.UserState;
import dev.danilscheglov.quasarmedbot.model.enums.State;
import dev.danilscheglov.quasarmedbot.util.BotMessages;
import dev.danilscheglov.quasarmedbot.util.BotPatterns;
import dev.danilscheglov.quasarmedbot.util.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dev.danilscheglov.quasarmedbot.util.BotUtils.escapeMarkdown;

@Service
public class MessageProcessorService {

    private final Map<Long, UserState> userStates = new ConcurrentHashMap<>();
    private final Map<Long, UserData> userData = new ConcurrentHashMap<>();

    private final CrpApiService crpApiService;

    public MessageProcessorService(CrpApiService crpApiService) {
        this.crpApiService = crpApiService;
    }

    public List<String> processMessage(long chatId, String text) {
        if (text.startsWith("/")) {
            String cmdResponse = processCommand(chatId, text);
            return Arrays.asList(cmdResponse, BotMessages.REQUEST_NAME);
        }

        UserState state = userStates.computeIfAbsent(chatId, id -> new UserState(State.AWAITING_NAME));

        return switch (state.getState()) {
            case AWAITING_NAME -> processName(chatId, text, state);
            case AWAITING_BIRTHDATE -> processBirthdate(chatId, text, state);
            case AWAITING_PRESSURE -> processPressureReading(chatId, text, state);
        };
    }

    private String processCommand(long chatId, String command) {
        if ("/start".equalsIgnoreCase(command)) {
            userStates.put(chatId, new UserState(State.AWAITING_NAME));
            userData.remove(chatId);
            return BotMessages.START_MESSAGE;
        }
        return BotMessages.UNKNOWN_COMMAND;
    }

    private List<String> processName(long chatId, String text, UserState state) {
        if (!text.matches(BotPatterns.NAME_PATTERN)) {
            return Collections.singletonList(BotMessages.REQUEST_NAME);
        }

        String[] parts = text.trim().split("\\s+");
        if (parts.length < 1 || parts.length > 3) {
            return Collections.singletonList(BotMessages.INVALID_NAME_PARTS);
        }

        String lastName = parts[0];
        String firstName = parts.length > 1 ? parts[1] : "";
        String middleName = parts.length > 2 ? parts[2] : "";

        userData.put(chatId, new UserData(lastName, firstName, middleName, null));
        state.setState(State.AWAITING_BIRTHDATE);
        userStates.put(chatId, state);

        return Arrays.asList(BotMessages.NAME_RECORDED + escapeMarkdown(text), BotMessages.REQUEST_BIRTHDATE);
    }

    private List<String> processBirthdate(long chatId, String text, UserState state) {
        if (!text.matches(BotPatterns.DATE_PATTERN)) {
            return Collections.singletonList(BotMessages.REQUEST_BIRTHDATE);
        }

        try {
            LocalDate birthdate = LocalDate.parse(text, DateUtils.UI_FORMATTER);
            if (!DateUtils.isDateInValidRange(birthdate)) {
                return Collections.singletonList(BotMessages.INVALID_BIRTHDATE_RANGE);
            }

            userData.computeIfPresent(chatId, (id, d) -> new UserData(d.getLastName(), d.getFirstName(), d.getMiddleName(), birthdate));

            state.setState(State.AWAITING_PRESSURE);
            userStates.put(chatId, state);

            return Arrays.asList(BotMessages.BIRTHDATE_RECORDED + escapeMarkdown(text), BotMessages.REQUEST_PRESSURE);
        } catch (DateTimeParseException ex) {
            return Collections.singletonList(BotMessages.INVALID_BIRTHDATE_FORMAT);
        }
    }

    private List<String> processPressureReading(long chatId, String text, UserState state) {
        if (!text.matches(BotPatterns.PRESSURE_PATTERN)) {
            return Collections.singletonList(BotMessages.INVALID_PRESSURE_FORMAT);
        }

        try {
            String[] parts = text.split("/");
            int systolic = Integer.parseInt(parts[0]);
            int diastolic = Integer.parseInt(parts[1]);

            if (systolic < 90 || systolic > 200 || diastolic < 60 || diastolic > 120) {
                return Collections.singletonList(BotMessages.PRESSURE_OUT_OF_RANGE);
            }

            userData.computeIfPresent(chatId, (id, d) -> {
                d.setPressure(text);
                return d;
            });

            UserData u = userData.get(chatId);
            crpApiService.searchByFioAndBirthdate(u.getLastName(), u.getFirstName(), u.getMiddleName(), u.getBirthdate().format(DateUtils.API_FORMATTER));

            state.setState(State.AWAITING_NAME);
            userStates.put(chatId, state);

            return Arrays.asList(BotMessages.PRESSURE_RECORDED + escapeMarkdown(text), BotMessages.DATA_SENT);
        } catch (NumberFormatException ex) {
            return Collections.singletonList(BotMessages.PRESSURE_PARSE_ERROR);
        }
    }
}
