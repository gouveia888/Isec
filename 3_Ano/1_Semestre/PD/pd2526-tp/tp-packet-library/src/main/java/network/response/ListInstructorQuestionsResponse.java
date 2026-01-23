package network.response;

import network.data.QuestionDTO;
import network.enums.QuestionFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record ListInstructorQuestionsResponse(
        boolean success,
        String message,
        QuestionFilter filter,
        List<QuestionDTO> questionDTOS
) implements Serializable {

    public static ListInstructorQuestionsResponse success(QuestionFilter filter, List<QuestionDTO> questionDTOS){
        return new ListInstructorQuestionsResponse(true, "Obtained successfully.", filter, questionDTOS);
    }

    public static ListInstructorQuestionsResponse failure(String message){
        return new ListInstructorQuestionsResponse(false, message, QuestionFilter.ALL, new ArrayList<>());
    }

}
