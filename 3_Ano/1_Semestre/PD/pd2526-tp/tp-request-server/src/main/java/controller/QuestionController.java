package controller;

import database.DatabaseManager;
import model.*;
import network.data.AnswerQuestionDTO;
import network.data.QuestionDTO;
import network.data.QuestionStatisticsDTO;
import network.data.StudentAnswerDTO;
import network.enums.QuestionFilter;
import network.request.*;
import network.response.*;
import org.sqlite.util.QueryUtils;
import service.QuestionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class QuestionController {
    private DatabaseManager databaseManager;
    private final QuestionService questionService;

    public QuestionController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.questionService = new QuestionService(databaseManager);
    }


    public CreateQuestionResponse createQuestion(CreateQuestionRequest request, UserController userController) {
        QuestionDTO questionDTO = request.questionDTO();

        // validate user
        User activeUser = userController.getActiveUser();
        if (activeUser == null) {
            return CreateQuestionResponse.failure("Not logged in");
        }
        if (!(activeUser instanceof Instructor)) {
            return CreateQuestionResponse.failure("Only instructors can create questions");
        }
        Question question = Question.fromDTO(questionDTO, activeUser.getId());

        try {
            int id = questionService.createQuestion(question);
            return CreateQuestionResponse.success("Question created", id);
        } catch (IllegalArgumentException e) {
            return CreateQuestionResponse.failure(e.getMessage());
        }
    }

    public EditQuestionResponse editQuestion(EditQuestionRequest request, UserController userController) {
        QuestionDTO questionDTO = request.questionDTO();

        // validate user
        User activeUser = userController.getActiveUser();
        if (activeUser == null) {
            return EditQuestionResponse.failure("Not logged in");
        }
        if (!(activeUser instanceof Instructor)) {
            return EditQuestionResponse.failure("Only instructors can edit questions");
        }

        // check if the question is from this user
        Question oldQuestion = questionService.getQuestionById(questionDTO.getQuestionId());
        if (oldQuestion == null) {
            return EditQuestionResponse.failure("Question does not exist.");
        }
        if (oldQuestion.getInstructorId() != activeUser.getId()) {
            return EditQuestionResponse.failure("Can only edit your own questions.");
        }
        Question newQuestion = Question.fromDTO(questionDTO, activeUser.getId());

        // check if the question can be edited
        if (!questionService.canEditQuestion(newQuestion.getQuestionId())) {
            return EditQuestionResponse.failure("Can't edit question that already has answers.");
        }

        try {
            boolean ok = questionService.updateQuestion(newQuestion);
            if (ok)
                return EditQuestionResponse.success("Question updated.", newQuestion.getQuestionId());
            else
                return EditQuestionResponse.failure("Couldn't update the question.");
        } catch (IllegalArgumentException e) {
            return EditQuestionResponse.failure(e.getMessage());
        }
    }

    public DeleteQuestionResponse deleteQuestion(DeleteQuestionRequest request, UserController userController) {
        // validate user
        User activeUser = userController.getActiveUser();
        if (activeUser == null) {
            return DeleteQuestionResponse.failure("Not logged in");
        }
        if (!(activeUser instanceof Instructor)) {
            return DeleteQuestionResponse.failure("Only instructors can edit questions");
        }

        // check if the question is from this user
        Question oldQuestion = questionService.getQuestionById(request.questionId());
        if (oldQuestion == null) {
            return DeleteQuestionResponse.failure("Question does not exist.");
        }
        if (oldQuestion.getInstructorId() != activeUser.getId()) {
            return DeleteQuestionResponse.failure("Can only delete your own questions.");
        }

        // check if the question can be delete
        if (!questionService.canEditQuestion(request.questionId())) {
            return DeleteQuestionResponse.failure("Can't delete question that already has answers.");
        }

        try {
            boolean ok = questionService.deleteQuestion(request.questionId());
            if (ok)
                return DeleteQuestionResponse.success("Question deleted.");
            else
                return DeleteQuestionResponse.failure("Couldn't delete the question.");
        } catch (IllegalArgumentException e) {
            return DeleteQuestionResponse.failure(e.getMessage());
        }
    }

    public ListInstructorQuestionsResponse listInstructorQuestions(ListInstructorQuestionsRequest listInstructorQuestionsRequest, UserController userController) {
        User user = userController.getActiveUser();
        if (!(user instanceof Instructor)) {
            ListInstructorQuestionsResponse.failure("Not authorized.");
        }

        List<Question> questionList = questionService.getQuestionsByInstructor(user.getId(), listInstructorQuestionsRequest.filter());
        List<QuestionDTO> dtoList = questionList.stream().map(Question::toDTO).toList();
        return ListInstructorQuestionsResponse.success(QuestionFilter.ALL, dtoList);
    }

    private QuestionStatisticsDTO buildQuestionStatisticsDTO(UserController userController, Question question) {
        // If everything went well, lets prepare the data
        AtomicBoolean success = new AtomicBoolean(true);
        List<StudentAnswer> studentAnswers = questionService.getQuestionAnswers(question.getQuestionId());
        List<StudentAnswerDTO> studentAnswerDTOS = studentAnswers.stream().map(
                ans -> {
                    Student s = userController.getStudent(ans.getStudentId());
                    if (s == null) {
                        success.set(false);
                        return null;
                    }
                    return new StudentAnswerDTO(
                            s.getStudentNumber(),
                            s.getName(),
                            s.getEmail(),
                            ans.getSelectedOptionIndex()
                    );
                }
        ).toList();
        if (!success.get()) return null;

        QuestionDTO questionDTO = question.toDTO();
        return new QuestionStatisticsDTO(questionDTO, studentAnswerDTOS);
    }

    public ViewQuestionStatisticsResponse viewQuestionStatistics(ViewQuestionStatisticsRequest request, UserController userController) {
        User user = userController.getActiveUser();
        if (!(user instanceof Instructor)) {
            return ViewQuestionStatisticsResponse.failure("Only instructors can view this.");
        }

        Question question = questionService.getQuestionById(request.questionId());
        if (question == null) {
            return ViewQuestionStatisticsResponse.failure("There is no such question.");
        }
        if (question.determineStatus() != QuestionFilter.EXPIRED) {
            return ViewQuestionStatisticsResponse.failure("Can only view expired questions.");
        }

        QuestionStatisticsDTO questionStatisticsDTO = buildQuestionStatisticsDTO(userController, question);
        if (questionStatisticsDTO != null) {
            return ViewQuestionStatisticsResponse.success(questionStatisticsDTO);
        } else {
            return ViewQuestionStatisticsResponse.failure("Couldn't get question details.");
        }

    }


    public ShowQuestionOptionsResponse studentViewQuestionChoices(ShowQuestionOptionsRequest request, UserController userController) {
        User user = userController.getActiveUser();
        if (!(user instanceof Student)) {
            return new ShowQuestionOptionsResponse(null, false, "Only students can answer the questions!");
        }

        Question q = questionService.getQuestionById(request.accessCode());
        if (q == null) {
            return new ShowQuestionOptionsResponse(null, false, "Invalid access code!");
        }

        if (q.determineStatus() != QuestionFilter.ACTIVE)
            return new ShowQuestionOptionsResponse(null, false, "Question is not active!");

        boolean hasAnswered = questionService.getQuestionAnswers(q.getQuestionId()).stream().anyMatch(p -> p.getStudentId() == user.getId());
        if (hasAnswered) {
            return new ShowQuestionOptionsResponse(null, false, "Already answered question!");
        }

        q.setCorrectOptionIndex(-1); //dont show the correct option to the UI
        return new ShowQuestionOptionsResponse(q.toDTO(), true, "");

    }

    public SubmitQuestionAnswerResponse studentSubmitQuestionAnswer(SubmitQuestionAnswerRequest request, UserController userController) {
        User user = userController.getActiveUser();
        if (!(user instanceof Student)) {
            return new SubmitQuestionAnswerResponse(false, "Only student can answer the questions!");
        }

        Question q = questionService.getQuestionById(request.questionId());
        if (q == null) {
            return new SubmitQuestionAnswerResponse(false, "Question not exists!");
        }
        if (q.determineStatus() != QuestionFilter.ACTIVE)
            return new SubmitQuestionAnswerResponse(false, "Question is not active!");
        boolean hasAnswered = questionService.getQuestionAnswers(q.getQuestionId()).stream().anyMatch(p -> p.getStudentId() == user.getId());
        if (hasAnswered) {
            return new SubmitQuestionAnswerResponse(false, "Already answered question!");
        }

        boolean ok = questionService.submitAnswer(q.getQuestionId(), request.answerId(), user.getId());
        if (ok) return new SubmitQuestionAnswerResponse(true, "Question answered successfully!");
        return new SubmitQuestionAnswerResponse(false, "Current answer failed!");
    }

    public ShowQuestionsAnswerResponse studentShowQuestionAnswers(ShowQuestionsAnswerRequest request, UserController userController) {
        User user = userController.getActiveUser();
        if (!(user instanceof Student)) {
            return new ShowQuestionsAnswerResponse(null, "Only student can answer the questions!", false);
        }

        List<StudentAnswer> answerList = questionService.getStudentQuestionsAnswers(user.getId());
        List<AnswerQuestionDTO> dtoList = answerList.stream().map(
                ans -> new AnswerQuestionDTO(
                        questionService.getQuestionById(ans.getQuestionId()).toDTO(),
                        ans.getSelectedOptionIndex()
                )
        ).toList();
        return new ShowQuestionsAnswerResponse(dtoList, "", true);
    }
}
