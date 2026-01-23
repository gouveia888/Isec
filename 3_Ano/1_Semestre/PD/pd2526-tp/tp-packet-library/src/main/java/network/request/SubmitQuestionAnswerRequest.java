package network.request;

import java.io.Serializable;

public record SubmitQuestionAnswerRequest(int questionId, int answerId) implements Serializable {
}
