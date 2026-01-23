package model;

public class StudentAnswer {
    private int answerId;
    private int questionId;
    private int studentId;
    private int selectedOptionIndex;


    public StudentAnswer(int answerId, int questionId, int studentId, int selectedOptionIndex) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.studentId = studentId;
        this.selectedOptionIndex = selectedOptionIndex;
    }

    public StudentAnswer() {}

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    public void setSelectedOptionIndex(int selectedOptionIndex) {
        this.selectedOptionIndex = selectedOptionIndex;
    }

}