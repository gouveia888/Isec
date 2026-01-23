package network.response;

import java.io.Serializable;

public record CreateQuestionResponse(
        boolean success,
        String message,
        int accessCode
) implements Serializable {

    public static CreateQuestionResponse success(String message, int accessCode) {
        return new CreateQuestionResponse(true, message, accessCode);
    }

    public static CreateQuestionResponse failure(String message) {
        return new CreateQuestionResponse(false, message, -1);
    }
}
