package dev.danilscheglov.quasarmedbot.util;

public class BotPatterns {

    public static final String NAME_PATTERN = "^[a-zA-Zа-яА-Я\\s]+$";
    public static final String DATE_PATTERN = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
    public static final String PRESSURE_PATTERN = "^\\d+/\\d+$";

    private BotPatterns() {

    }
}
