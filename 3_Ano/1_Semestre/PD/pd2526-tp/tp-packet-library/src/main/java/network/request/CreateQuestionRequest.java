package network.request;

import network.data.QuestionDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record CreateQuestionRequest(
        QuestionDTO questionDTO
) implements Serializable {
}
