package dev.danilscheglov.quasarmedbot.util;

public class BotUtils {

    public static String escapeMarkdown(String text) {
        return text.replace(".", "\\.")
                .replace("-", "\\-")
                .replace("!", "\\!");
    }

    private BotUtils() {

    }
}
