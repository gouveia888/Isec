package network.data;

import java.io.Serializable;

public class StudentAnswerDTO implements Serializable {
    String studentNumber;
    String studentName;
    String studentEmail;
    int studentQuestionAnswer;

    public StudentAnswerDTO(String studentNumber, String studentName, String studentEmail, int studentQuestionAnswer) {
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentQuestionAnswer = studentQuestionAnswer;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public int getStudentQuestionAnswer() {
        return studentQuestionAnswer;
    }

    public void setStudentQuestionAnswer(int studentQuestionAnswer) {
        this.studentQuestionAnswer = studentQuestionAnswer;
    }
}
