package service;

import dao.interfaces.QuestionDAO;
import dao.interfaces.StudentAnswerDAO;
import dao.sqlite.SQLiteQuestionDAO;
import dao.sqlite.SQLiteStudentAnswerDAO;
import database.DatabaseManager;
import model.Question;
import model.StudentAnswer;
import network.enums.QuestionFilter;
import util.DateTimeUtil;
import util.FancyLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {
    private DatabaseManager databaseManager;
    private QuestionDAO questionDAO;
    private StudentAnswerDAO studentAnswerDAO;


    public QuestionService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.questionDAO = new SQLiteQuestionDAO(databaseManager);
        this.studentAnswerDAO = new SQLiteStudentAnswerDAO(databaseManager);
    }

    // ----------
    // Instructor
    // ----------

    public int createQuestion(Question question) {
        validateQuestion(question);
        FancyLog.println("Question validated!");
        return questionDAO.createQuestion(question);
    }

    public boolean updateQuestion(Question question) {
        validateQuestion(question);
        return questionDAO.updateQuestion(question);
    }

    public boolean deleteQuestion(int questionId) {
        return questionDAO.deleteQuestion(questionId);
    }

    public Question getQuestionById(int questionId) {
        return questionDAO.getQuestionById(questionId);
    }

    public List<Question> getQuestionsByInstructor(int instructorId, QuestionFilter filter) {
        return questionDAO.getQuestionsByInstructor(instructorId, filter);
    }

    public List<Question> getAllQuestions() {
        return questionDAO.getAllQuestions();
    }

    public List<Question> getFilteredQuestions(QuestionFilter filter){
        return questionDAO.getFilteredQuestions(filter);
    }

    public boolean canEditQuestion(int questionId){
        return getQuestionAnswers(questionId).isEmpty();
    }

    public List<StudentAnswer> getQuestionAnswers(int questionId){
        return studentAnswerDAO.getAnswersForQuestion(questionId);
    }

    // -------
    // Student
    // -------

    public boolean submitAnswer(int questionId, int selectedOptionIndex, int studentId) {
        Question question = questionDAO.getQuestionById(questionId);
        if (question == null) return false;

        StudentAnswer answer = new StudentAnswer(-1, questionId, studentId, selectedOptionIndex);
        return studentAnswerDAO.submitAnswer(answer) != -1;
    }

    public List<StudentAnswer> getStudentQuestionsAnswers(int studentId) {
        List<StudentAnswer> studentAnswers = studentAnswerDAO.getAnswersByStudent(studentId);
        List<StudentAnswer> expiredQuestions = new ArrayList<>();

        for (StudentAnswer answer : studentAnswers) {
            Question question = questionDAO.getQuestionById(answer.getQuestionId());
            if (question.determineStatus() == QuestionFilter.EXPIRED) {
                expiredQuestions.add(answer);
            }
        }
        return expiredQuestions;
    }




    // -------------------------------------------------------------
    // VALIDATION
    // -------------------------------------------------------------

    private void validateQuestion(Question q) {

//        if (q.getAccessCode() == null || q.getAccessCode().isEmpty())
//            throw new IllegalArgumentException("Access code cannot be empty.");

        if (q.getStatement() == null || q.getStatement().isBlank())
            throw new IllegalArgumentException("Question statement cannot be empty.");

        if (q.getInstructorId() < 0)
            throw new IllegalArgumentException("Instructor id cannot be empty.");

        if (q.getStartTime() == null || q.getEndTime() == null)
            throw new IllegalArgumentException("Start and end times cannot be null.");


        LocalDateTime start = DateTimeUtil.fromString(q.getStartTime());
        LocalDateTime end = DateTimeUtil.fromString(q.getEndTime());
        LocalDateTime now = LocalDateTime.now();

        if (start.isBefore(now) || end.isBefore(now)) {
            throw new IllegalArgumentException("Start and end times cannot end before the present moment.");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        if (q.getCorrectOptionIndex() < 0)
            throw new IllegalArgumentException("Correct option index must be >= 0.");

        if (q.getOptions() == null || q.getOptions().isEmpty())
            throw new IllegalArgumentException("A question must have at least one option.");

        if (q.getCorrectOptionIndex() >= q.getOptions().size())
            throw new IllegalArgumentException("Correct option index is out of bounds.");
    }


}
