package network.response;

import network.data.QuestionStatisticsDTO;

import java.io.Serializable;

public record ViewQuestionStatisticsResponse(
        boolean success,
        String message,
        QuestionStatisticsDTO questionStatisticsDTO
) implements Serializable {

    public static ViewQuestionStatisticsResponse success(QuestionStatisticsDTO questionStatisticsDTO) {
        return new ViewQuestionStatisticsResponse(
                true,
                "Obtained details successfully.",
                questionStatisticsDTO
        );
    }

    public static ViewQuestionStatisticsResponse failure(String message){
        return new ViewQuestionStatisticsResponse(false, message, null);
    }
}
