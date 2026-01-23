package network.response;

import network.data.AnswerQuestionDTO;

import java.io.Serializable;
import java.util.List;

public record ShowQuestionsAnswerResponse(
        List<AnswerQuestionDTO> list,
        String message,
        boolean success
) implements Serializable {
}
