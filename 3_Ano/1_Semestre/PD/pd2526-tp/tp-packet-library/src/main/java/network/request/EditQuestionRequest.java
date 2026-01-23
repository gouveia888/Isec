package network.request;

import network.data.QuestionDTO;

import java.io.Serializable;

public record EditQuestionRequest(
        QuestionDTO questionDTO
) implements Serializable {
}
