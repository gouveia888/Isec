package dao.sqlite;

import dao.BaseDAO;
import dao.interfaces.QuestionDAO;
import database.DatabaseManager;
import model.Question;
import network.enums.QuestionFilter;
import util.DateTimeUtil;
import util.FancyLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLiteQuestionDAO extends BaseDAO implements QuestionDAO {

    public SQLiteQuestionDAO(DatabaseManager dbManager) {
        super(dbManager);
    }

    @Override
    public int createQuestion(Question question) {
        try {
            int qId = executeInsertAndReturnKey(
                    "INSERT INTO questions (statement, start_time, end_time, instructor_id, correct_option_index) " +
                    "VALUES (?, ?, ?, ?, ?)",
                    question.getStatement(),
                    question.getStartTime(),
                    question.getEndTime(),
                    question.getInstructorId(),
                    question.getCorrectOptionIndex()
            );

            // Insert options
            List<String> opts = question.getOptions();
            for (int i = 0; i < opts.size(); i++) {
                executeWrite(
                        "INSERT INTO question_options (question_id, option_index, option_text) VALUES (?, ?, ?)",
                        qId, i, opts.get(i)
                );
            }

            return qId;

        } catch (SQLException e) {
            throw new RuntimeException("Error creating question: " + e.getMessage(), e);
        }
    }


    @Override
    public boolean updateQuestion(Question q) {
        try {
            executeWrite(
                    "UPDATE questions SET statement=?, start_time=?, end_time=?, instructor_id=?, correct_option_index=? " +
                    "WHERE question_id=?",
                    q.getStatement(),
                    q.getStartTime(),
                    q.getEndTime(),
                    q.getInstructorId(),
                    q.getCorrectOptionIndex(),
                    q.getQuestionId()
            );

            // Delete old options
            executeWrite("DELETE FROM question_options WHERE question_id=?", q.getQuestionId());

            // Reinsert updated options
            List<String> opts = q.getOptions();
            for (int i = 0; i < opts.size(); i++) {
                executeWrite(
                        "INSERT INTO question_options (question_id, option_index, option_text) VALUES (?, ?, ?)",
                        q.getQuestionId(), i, opts.get(i)
                );
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating question: " + e.getMessage(), e);
        }
    }


    @Override
    public boolean deleteQuestion(int questionId) {
        try {
            // we have "ON DELETE CASCADE" so we shouldn't need this line, but it doesn't work for some reason so it's here
            executeWrite("DELETE FROM question_options WHERE question_id=?", questionId);

            executeWrite("DELETE FROM questions WHERE question_id=?", questionId);
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting question: " + e.getMessage(), e);
        }
    }

    @Override
    public Question getQuestionById(int questionId) {
        try {
            ResultSet rs = executeQuery(
                    "SELECT * FROM questions WHERE question_id=?", questionId
            );

            if (!rs.next()) return null;

            Question q = mapQuestion(rs);
            q.setOptions(loadOptions(questionId));
            return q;

        } catch (SQLException e) {
            FancyLog.println("Error getting question.");
            throw new RuntimeException("Error fetching question: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Question> getQuestionsByInstructor(int instructorId) {
        List<Question> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(
                    "SELECT * FROM questions WHERE instructor_id=? ORDER BY question_id DESC",
                    instructorId
            );

            while (rs.next()) {
                Question q = mapQuestion(rs);
                q.setOptions(loadOptions(q.getQuestionId()));
                list.add(q);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching questions: " + e.getMessage(), e);
        }
        return list;
    }


    @Override
    public List<Question> getQuestionsByInstructor(int instructorId, QuestionFilter filter) {
        List<Question> list = getQuestionsByInstructor(instructorId); // updated to use int ID
        if (filter == QuestionFilter.ALL) {
            return list;
        }
        return list.stream().filter(q -> q.determineStatus() == filter).toList();
    }

    @Override
    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(
                    "SELECT * FROM questions ORDER BY question_id DESC"
            );

            while (rs.next()) {
                Question q = mapQuestion(rs);
                q.setOptions(loadOptions(q.getQuestionId()));
                list.add(q);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all questions: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Question> getFilteredQuestions(QuestionFilter filter) {
        List<Question> list = getAllQuestions();
        if (filter == QuestionFilter.ALL) return list;
        return list.stream().filter(q -> q.determineStatus() == filter).toList();
    }

    // ---------------- Helper Methods ---------------- //

    private List<String> loadOptions(int questionId) throws SQLException {
        List<String> options = new ArrayList<>();
        ResultSet rs = executeQuery(
                "SELECT option_text FROM question_options WHERE question_id=? ORDER BY option_index ASC",
                questionId
        );
        while (rs.next()) {
            options.add(rs.getString("option_text"));
        }
        return options;
    }

    private Question mapQuestion(ResultSet rs) throws SQLException {
        Question q = new Question();
        q.setQuestionId(rs.getInt("question_id"));
        q.setStatement(rs.getString("statement"));
        q.setStartTime(rs.getString("start_time"));
        q.setEndTime(rs.getString("end_time"));
        q.setInstructorId(rs.getInt("instructor_id"));
        q.setCorrectOptionIndex(rs.getInt("correct_option_index"));
        return q;
    }
}

