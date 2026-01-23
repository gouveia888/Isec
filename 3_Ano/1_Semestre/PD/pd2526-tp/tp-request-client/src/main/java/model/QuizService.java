package model;

import network.data.AnswerQuestionDTO;
import network.data.QuestionDTO;
import network.enums.QuestionFilter;
import network.request.*;
import network.response.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizService {
    private QuizAppManager appManager;

    private List<QuestionDTO> questionDTOS;
    private QuestionDTO activeQuestion;
    private List<AnswerQuestionDTO> answersDTOS;

    public QuizService(QuizAppManager appManager) {
        this.appManager = appManager;
        questionDTOS = new ArrayList<>();
        answersDTOS = new ArrayList<>();
    }

    public void createQuestion(String statement, List<String> options, int correctOption, LocalDateTime start, LocalDateTime end) {
        CreateQuestionRequest request = new CreateQuestionRequest(new QuestionDTO(
                statement,
                options,
                correctOption,
                start,
                end
        ));
        appManager.sendToServer(request);
    }

    public void editQuestion(int id, String statement, List<String> options, int correctOption, LocalDateTime start, LocalDateTime end) {
        EditQuestionRequest request = new EditQuestionRequest(new QuestionDTO(
                id,
                statement,
                options,
                correctOption,
                start,
                end,
                QuestionFilter.ALL
        ));
        appManager.sendToServer(request);
    }

    public void getQuestionByAccessCode(int code) {
        ShowQuestionOptionsRequest request = new ShowQuestionOptionsRequest(code);
        appManager.sendToServer(request);
    }

    public QuestionDTO getActiveQuestion() {
        return activeQuestion;
    }

    public List<AnswerQuestionDTO> getAnswersDTOS() {
        return answersDTOS;
    }

    public void submitResponse(int currentQuestion, int selectedOption) {
        SubmitQuestionAnswerRequest request = new SubmitQuestionAnswerRequest(currentQuestion, selectedOption);
        appManager.sendToServer(request);
    }


    public void getInstructorQuestions(QuestionFilter questionFilter) {
        ListInstructorQuestionsRequest request = new ListInstructorQuestionsRequest(questionFilter);
        appManager.sendToServer(request);
    }

    // response
    public void listInstructorQuestionsResponse(ListInstructorQuestionsResponse response) {
        this.questionDTOS = response.questionDTOS();
    }

    public List<QuestionDTO> getQuestions() {
        return questionDTOS;
    }

    public void createQuestionResponse(CreateQuestionResponse response) {

    }

    public void showQuestionResponse(ShowQuestionOptionsResponse response) {
        if(!response.exist())
            return;

        activeQuestion = response.question();

    }

    public void submitAnswerResponse(SubmitQuestionAnswerResponse response) {
        activeQuestion = null;
    }

    public void showQuestionsAnswers() {
        ShowQuestionsAnswerRequest request = new ShowQuestionsAnswerRequest();
        appManager.sendToServer(request);
    }

    public void showQuestionsAnswersResponse(ShowQuestionsAnswerResponse response) {
        this.answersDTOS = response.list();
    }

    public void editQuestionResponse(EditQuestionResponse response) {
    }

    public void deleteQuestion(int questionId) {
        DeleteQuestionRequest request = new DeleteQuestionRequest(questionId);
        appManager.sendToServer(request);
    }

    public void deleteQuestionResponse(DeleteQuestionResponse response) {

    }

    public void viewQuestionStatistics(int questionId) {
        ViewQuestionStatisticsRequest request = new ViewQuestionStatisticsRequest(questionId);
        appManager.sendToServer(request);
    }

    public void viewQuestionStatisticsResponse(ViewQuestionStatisticsResponse response) {
    }
}
