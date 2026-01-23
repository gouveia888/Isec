package dao.interfaces;

import model.StudentAnswer;

import java.util.List;

public interface StudentAnswerDAO {

    int submitAnswer(StudentAnswer answer);
    boolean updateAnswer(StudentAnswer answer);
    boolean deleteAnswer(int answerId);

    StudentAnswer getAnswer(int questionId, int studentId);
    List<StudentAnswer> getAnswersForQuestion(int questionId);
    List<StudentAnswer> getAnswersByStudent(int studentId);
}
