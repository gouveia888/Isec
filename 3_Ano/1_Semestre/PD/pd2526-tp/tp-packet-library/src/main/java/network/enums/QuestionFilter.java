package network.enums;

import java.util.Locale;

public enum QuestionFilter {
    ALL,
    ACTIVE,
    FUTURE,
    EXPIRED;


    @Override
    public String toString() {
        String n = name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(n.charAt(0)) + n.substring(1);
    }
}
