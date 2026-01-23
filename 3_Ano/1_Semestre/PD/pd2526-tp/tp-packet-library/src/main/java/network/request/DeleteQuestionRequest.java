package network.request;

import java.io.Serializable;

public record DeleteQuestionRequest(
        int questionId
) implements Serializable {
}
