package dao.interfaces;


import model.Question;
import network.enums.QuestionFilter;

import java.util.List;


public interface QuestionDAO {

    int createQuestion(Question question);
    boolean updateQuestion(Question question);
    boolean deleteQuestion(int questionId);

    Question getQuestionById(int questionId);
    List<Question> getQuestionsByInstructor(int instructorId);
    List<Question> getQuestionsByInstructor(int instructorId, QuestionFilter filter);
    List<Question> getAllQuestions();
    List<Question> getFilteredQuestions(QuestionFilter filter);
}