package ui.views.data;

import network.data.AnswerQuestionDTO;

public class AnswerQuestionData {
    private int questionId;
    private String statement;
    private String answer;
    private String correct;
    private String endDate;

    public AnswerQuestionData(int questionId, String statement, String answer, String correct, String endDate) {
        this.questionId = questionId;
        this.statement = statement;
        this.answer = answer;
        this.correct = correct;
        this.endDate = endDate;
    }

    public static AnswerQuestionData fromAnswerQuestionDto(AnswerQuestionDTO answerQuestionDTO) {
        boolean isCorrect = answerQuestionDTO.getQuestion().getCorrectOption() == answerQuestionDTO.getSelectedOption();
        return new AnswerQuestionData(
                answerQuestionDTO.getQuestion().getQuestionId(),
                answerQuestionDTO.getQuestion().getStatement(),
                answerQuestionDTO.getQuestion().getOptions().get(answerQuestionDTO.getSelectedOption()),
                isCorrect ? "Correct" : "Incorrect",
                answerQuestionDTO.getQuestion().getEndDate().toString());
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getStatement() {
        return statement;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCorrect() {
        return correct;
    }

    public String getEndDate() {
        return endDate;
    }
}
