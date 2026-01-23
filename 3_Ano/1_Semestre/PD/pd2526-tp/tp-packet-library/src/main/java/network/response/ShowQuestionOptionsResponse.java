package network.response;

import network.data.QuestionDTO;

import java.io.Serializable;

public record ShowQuestionOptionsResponse(
        QuestionDTO question,
        boolean exist,
        String message
) implements Serializable {
}
