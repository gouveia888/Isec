package network.response;

import java.io.Serializable;

public record SubmitQuestionAnswerResponse(boolean success, String message) implements Serializable {
}
