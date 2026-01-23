package network.data;

import network.enums.QuestionFilter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a multiple-choice question created by an instructor.
 */
public class QuestionDTO implements Serializable {
    private int questionId;
    private String statement; // The question's statement (enunciado)
    private List<String> options; // Key: option index (1, 2, 3...), Value: option text
    private int correctOption; // The index of the correct option
    private LocalDateTime startDate; // Start date/time of the availability period
    private LocalDateTime endDate;   // End date/time of the availability period
    private QuestionFilter state;

    public QuestionDTO(String statement, List<String> options, int correctOption, LocalDateTime startDate, LocalDateTime endDate ) {
        this.statement = statement;
        this.options = options;
        this.correctOption = correctOption;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public QuestionDTO(int questionId,
                       String statement,
                       List<String> options,
                       int correctOption,
                       LocalDateTime startDate,
                       LocalDateTime endDate,
                       QuestionFilter state
    ) {
        this.questionId = questionId;
        this.statement = statement;
        this.options = options;
        this.correctOption = correctOption;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }

    /**
     * Checks if the question's availability period has ended.
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endDate);
    }

    /**
     * Checks if the question is currently available for students to answer.
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public QuestionFilter getState() {
        return state;
    }

    public void setState(QuestionFilter state) {
        this.state = state;
    }
}
