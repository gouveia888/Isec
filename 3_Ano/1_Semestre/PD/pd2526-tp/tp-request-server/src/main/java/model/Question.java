package model;

import network.data.QuestionDTO;
import network.enums.QuestionFilter;
import util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Question {

    private int questionId;
    //private String accessCode;
    private String statement;
    private String startTime;
    private String endTime;
    private int instructorId;

    /**
     * Index of the correct option (0..optionCount-1)
     */
    private int correctOptionIndex;

    /**
     * List of option texts in order (index-based)
     */
    private List<String> options = new ArrayList<>();

    // -------------------------
    // Constructors
    // -------------------------

    public Question() { }

    public Question(int questionId,
                    //String accessCode,
                    String statement,
                    String startTime,
                    String endTime,
                    int instructorId,
                    int correctOptionIndex, List<String> options) {

        this.questionId = questionId;
        //this.accessCode = accessCode;
        this.statement = statement;
        this.startTime = startTime;
        this.endTime = endTime;
        this.instructorId = instructorId;
        this.correctOptionIndex = correctOptionIndex;

        if (options != null) {
            this.options = new ArrayList<>(options);
        }
    }

    // -------------------------
    // Getters and setters
    // -------------------------

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = new ArrayList<>(options);
    }

    public void addOption(String optionText) {
        this.options.add(optionText);
    }

    public String getOption(int index) {
        return (index >= 0 && index < options.size()) ? options.get(index) : null;
    }

    public int getOptionCount() {
        return options.size();
    }

    @Override
    public String toString() {
        return "Question{" +
               "questionId=" + questionId +
               //", accessCode='" + accessCode + '\'' +
               ", statement='" + statement + '\'' +
               ", startTime='" + startTime + '\'' +
               ", endTime='" + endTime + '\'' +
               ", instructorId='" + instructorId + '\'' +
               ", correctOptionIndex=" + correctOptionIndex +
               ", options=" + options +
               '}';
    }

    public static Question fromDTO(QuestionDTO questionDTO, int instructorId){
        return new Question(
                questionDTO.getQuestionId(),
                questionDTO.getStatement(),
                questionDTO.getStartDate().toString(),
                questionDTO.getEndDate().toString(),
                instructorId,
                questionDTO.getCorrectOption() - 1, //the client is [1, N] the server is [0, N-1]
                questionDTO.getOptions()
        );
    }

    public QuestionDTO toDTO(){
        return new QuestionDTO(
                questionId,
                statement,
                options,
                correctOptionIndex,
                DateTimeUtil.fromString(getStartTime()),
                DateTimeUtil.fromString(getEndTime()),
                determineStatus()
        );
    }

    public QuestionFilter determineStatus() {
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime start = DateTimeUtil.fromString(getStartTime());
        LocalDateTime end = DateTimeUtil.fromString(getEndTime());

        if (current.isBefore(start)) {
            return QuestionFilter.FUTURE;
        } else if (current.isAfter(end)) {
            return QuestionFilter.EXPIRED;
        } else {
            return QuestionFilter.ACTIVE;
        }
    }
}
