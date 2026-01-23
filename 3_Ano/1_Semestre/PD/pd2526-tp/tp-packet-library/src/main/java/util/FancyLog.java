package util;


public class FancyLog {
    public enum Status {
        OK("OK", GREEN),
        FAILED("FAILED", RED),
        INFO("INFO", GREY)
        //WARNING("WARNING", YELLOW),
        ;

        private final String codeName;
        private final String color;

        Status(String codeName, String color) {
            this.codeName = codeName;
            this.color = color;
        }

        private static String centerString(String text, int width) {
            int spaceLeft = width - text.length();
            if (spaceLeft < 0) {
                return text;
            }

            int leftPadding = spaceLeft / 2;
            int rightPadding = spaceLeft - leftPadding;
            return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
        }

        @Override
        public String toString() {
            int maxWidth = 6;
            return String.format("[%s%s%s]",
                    color,
                    centerString(codeName, maxWidth),
                    RESET);
        }
    }

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREY = "\u001B[90m";

    public static void println(String message) {
        println(message, Status.INFO);
    }

    public static void println(String message, Status status) {
        String spacedMessage = message.replace("\n", "\n         ");
        System.out.printf("%s %s\n", status.toString(), spacedMessage);
    }
}
