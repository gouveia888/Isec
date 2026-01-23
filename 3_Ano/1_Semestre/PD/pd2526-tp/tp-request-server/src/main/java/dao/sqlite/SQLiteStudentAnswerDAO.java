package dao.sqlite;

import dao.BaseDAO;
import dao.interfaces.StudentAnswerDAO;
import database.DatabaseManager;
import model.StudentAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteStudentAnswerDAO
        extends BaseDAO
        implements StudentAnswerDAO {

    public SQLiteStudentAnswerDAO(DatabaseManager dbManager) {
        super(dbManager);
    }

    @Override
    public int submitAnswer(StudentAnswer answer) {
        try {
            return executeInsertAndReturnKey(
                    "INSERT INTO student_answers (question_id, student_id, selected_option_index) " +
                            "VALUES (?, ?, ?)",
                    answer.getQuestionId(),
                    answer.getStudentId(),
                    answer.getSelectedOptionIndex()
            );
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public boolean updateAnswer(StudentAnswer answer) {
        try {
            executeWrite(
                    "UPDATE student_answers SET selected_option_index=? WHERE answer_id=?",
                    answer.getSelectedOptionIndex(),
                    answer.getAnswerId()
            );
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating answer: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteAnswer(int answerId) {
        try {
            executeWrite("DELETE FROM student_answers WHERE answer_id=?", answerId);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting answer: " + e.getMessage(), e);
        }
    }

    @Override
    public StudentAnswer getAnswer(int questionId, int studentId) {
        try {
            ResultSet rs = executeQuery(
                    "SELECT * FROM student_answers WHERE question_id=? AND student_id=?",
                    questionId,
                    studentId
            );

            if (!rs.next()) return null;
            return mapAnswer(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching answer: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StudentAnswer> getAnswersForQuestion(int questionId) {
        List<StudentAnswer> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(
                    "SELECT * FROM student_answers WHERE question_id=?",
                    questionId
            );

            while (rs.next()) {
                list.add(mapAnswer(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching answers: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StudentAnswer> getAnswersByStudent(int studentId) {
        List<StudentAnswer> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(
                    "SELECT * FROM student_answers WHERE student_id=?",
                    studentId
            );

            while (rs.next()) {
                list.add(mapAnswer(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching answers: " + e.getMessage(), e);
        }
    }

    private StudentAnswer mapAnswer(ResultSet rs) throws SQLException {
        StudentAnswer a = new StudentAnswer();
        a.setAnswerId(rs.getInt("answer_id"));
        a.setQuestionId(rs.getInt("question_id"));
        a.setStudentId(rs.getInt("student_id"));
        a.setSelectedOptionIndex(rs.getInt("selected_option_index"));
        return a;
    }
}
