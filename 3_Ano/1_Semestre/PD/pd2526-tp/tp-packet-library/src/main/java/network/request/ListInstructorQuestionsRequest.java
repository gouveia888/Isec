package network.request;

import network.enums.QuestionFilter;

import java.io.Serializable;

public record ListInstructorQuestionsRequest(
        QuestionFilter filter
) implements Serializable {
}
