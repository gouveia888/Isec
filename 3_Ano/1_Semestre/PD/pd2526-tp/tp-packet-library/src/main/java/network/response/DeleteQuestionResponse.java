package network.response;

import java.io.Serializable;

public record DeleteQuestionResponse(
        boolean success,
        String message
) implements Serializable {
    public static DeleteQuestionResponse success(String message) {
        return new DeleteQuestionResponse(true, message);
    }

    public static DeleteQuestionResponse failure(String message) {
        return new DeleteQuestionResponse(false, message);
    }
}
