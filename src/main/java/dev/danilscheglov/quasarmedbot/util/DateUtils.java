package dev.danilscheglov.quasarmedbot.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter UI_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter API_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static boolean isDateInValidRange(LocalDate date) {
        LocalDate today = LocalDate.now();
        return !date.isAfter(today) && !date.isBefore(today.minusYears(100));
    }

    private DateUtils() {

    }
}
