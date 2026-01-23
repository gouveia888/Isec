package network.response;

import java.io.Serializable;

public record EditQuestionResponse(
        boolean success,
        String message,
        int accessCode
) implements Serializable {

    public static EditQuestionResponse success(String message, int accessCode) {
        return new EditQuestionResponse(true, message, accessCode);
    }

    public static EditQuestionResponse failure(String message) {
        return new EditQuestionResponse(false, message, -1);
    }
}
