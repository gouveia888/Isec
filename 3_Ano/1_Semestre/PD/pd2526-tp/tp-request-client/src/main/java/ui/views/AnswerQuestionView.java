package ui.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.QuizAppManager;
import network.data.QuestionDTO;
import network.response.SubmitQuestionAnswerResponse;
import ui.QuizApp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class AnswerQuestionView
    implements IClosableView
{
    private QuizApp app;
    private QuizAppManager appManager;
    private List<RadioButton> radioButtons;

    private Scene scene;
    private Stage stage;
    private Button submitButton;
    private Button cancelButton;
    private PropertyChangeListener submitAnswerResponseListener  = this::onSubmitAnswerResponse;

    public AnswerQuestionView(QuizApp app, QuizAppManager appManager) {
        this.app = app;
        this.appManager = appManager;


        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        QuestionDTO questionDTO = appManager.getQuizService().getActiveQuestion();

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        Label questionTitle = new Label(questionDTO.getStatement());
        questionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 0 0 15 0;");
        vBox.getChildren().add(questionTitle);

        ToggleGroup tg = new ToggleGroup();

        radioButtons = new ArrayList<>();
        for(String option: questionDTO.getOptions()){
            RadioButton radioButton = new RadioButton(option);
            radioButton.setToggleGroup(tg);
            vBox.getChildren().add(radioButton);
            radioButtons.add(radioButton);
        }

        submitButton = new Button("Submit");
        cancelButton = new Button("Cancel");
        vBox.getChildren().addAll(submitButton,cancelButton);

        this.scene = new Scene(vBox, 600, 400);
    }

    private void registerHandlers() {
        submitButton.setOnAction(this::onSubmitQuestion);
        cancelButton.setOnAction(_-> app.showStudentDashboard(this));
        appManager.addPropertyChangeListener(QuizAppManager.SUBMIT_QUESTION_ANSWER_RESPONSE, submitAnswerResponseListener);
    }


    @Override
    public void unregisterHandlers() {
        appManager.removePropertyChangeListener(QuizAppManager.SUBMIT_QUESTION_ANSWER_RESPONSE, submitAnswerResponseListener);
    }

    private void update() {

    }

    public Scene getScene() {
        return scene;
    }

    private void onSubmitQuestion(ActionEvent event) {
        int selectedOption = -1;
        for (RadioButton rb : radioButtons) {
            if (rb.isSelected()) {
                selectedOption = radioButtons.indexOf(rb);
                break;
            }
        }
        if (selectedOption == -1) {
            new Alert(Alert.AlertType.ERROR, "Please select an answer!").showAndWait();
        } else {
            QuestionDTO questionDTO = appManager.getQuizService().getActiveQuestion();
            appManager.getQuizService().submitResponse(questionDTO.getQuestionId(), selectedOption);
        }

    }

    private void onSubmitAnswerResponse(PropertyChangeEvent event) {
        Platform.runLater(() -> {
            if (event.getNewValue() instanceof SubmitQuestionAnswerResponse response) {
                if (response.success()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Answer successfully submitted!", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Answer failed!", ButtonType.CANCEL);
                    alert.showAndWait();
                }
                app.showStudentDashboard(this);
            }
        });
    }
}
