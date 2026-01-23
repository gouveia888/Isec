package ui.views;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.QuizAppManager;
import network.data.AnswerQuestionDTO;
import network.response.ShowQuestionOptionsResponse;
import network.response.ShowQuestionsAnswerResponse;
import ui.QuizApp;
import ui.views.data.AnswerQuestionData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class StudentDashboardView
    implements IClosableView
{

    private QuizApp app;
    private QuizAppManager appManager;
    private Scene scene;

    private List<AnswerQuestionDTO> answeredQuestionsList;

    private TextField accessCodeField = new TextField();
    TableView<AnswerQuestionData> answeredQuestionTable;
    TabPane tabPane;

    private PropertyChangeListener showQuestionListener;
    private PropertyChangeListener showAnswersListener;
    private ChangeListener<Tab> tabPaneChangeListener;

    public StudentDashboardView(QuizApp app, QuizAppManager appManager) {
        this.app = app;
        this.appManager = appManager;

        // --- Layout ---
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createCenterPanel());

        this.scene = new Scene(root, 800, 600);

        showQuestionListener = this::onShowQuestionResponse;
        showAnswersListener = this::onShowAnswerResponse;
        tabPaneChangeListener = this::onTabChangeListener;

        registerHandlers();
    }

    public void registerHandlers(){
        appManager.addPropertyChangeListener(QuizAppManager.SHOW_QUESTION_OPTIONS_RESPONSE, showQuestionListener);
        appManager.addPropertyChangeListener(QuizAppManager.SHOW_QUESTIONS_ANSWER_RESPONSE, showAnswersListener);
        tabPane.getSelectionModel().selectedItemProperty().addListener(tabPaneChangeListener);
    }

    @Override
    public void unregisterHandlers(){
        appManager.removePropertyChangeListener(QuizAppManager.SHOW_QUESTION_OPTIONS_RESPONSE, showQuestionListener);
        appManager.removePropertyChangeListener(QuizAppManager.SHOW_QUESTIONS_ANSWER_RESPONSE, showAnswersListener);
        tabPane.getSelectionModel().selectedItemProperty().removeListener(tabPaneChangeListener);
    }

    // --- Component Creation Methods ---

    private VBox createHeader() {
        Label welcomeLabel = new Label("Welcome, " + appManager.getAuthenticatorService().getUserName() + " (Student #" + appManager.getAuthenticatorService().getStudentNumber() + ")");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(_ -> handleLogout());

        Button editUserButton = new Button("Edit Account Details");
        editUserButton.setOnAction(_ -> app.showEditUserView(this));

        HBox header = new HBox(10, welcomeLabel, new Region(), logoutButton, editUserButton);
        HBox.setHgrow(new Region(), Priority.ALWAYS);
        header.setPadding(new Insets(10));

        return new VBox(header, new Separator());
    }

    private TabPane createCenterPanel() {
        tabPane = new TabPane();
        tabPane.getTabs().addAll(createAnswerQuestionTab(), createViewResultsTab());
        return tabPane;
    }

    private Tab createAnswerQuestionTab() {
        accessCodeField.setPromptText("Enter Instructor's Access Code (e.g., 12)");
        Button enterCodeButton = new Button("View Question");
        enterCodeButton.setMaxWidth(Double.MAX_VALUE);
        enterCodeButton.setOnAction(e -> handleEnterCode());

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));
        content.setMaxWidth(400);
        content.getChildren().addAll(
                new Label("Access a Question"),
                accessCodeField,
                enterCodeButton
        );

        StackPane tabContent = new StackPane(content);
        return new Tab("Answer New Question", tabContent);
    }

    private Tab createViewResultsTab() {
        answeredQuestionTable = new TableView<>();
        // Table columns setup (e.g., Statement, Completion Date, Correctness)
        TableColumn<AnswerQuestionData, Integer> accessCodeCol = new TableColumn<>("Access code");
        accessCodeCol.setCellValueFactory( cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getQuestionId()));
        TableColumn<AnswerQuestionData, String> questionCol = new TableColumn<>("Question");
        questionCol.setCellValueFactory( cell -> new ReadOnlyStringWrapper(cell.getValue().getStatement()));
        TableColumn<AnswerQuestionData, String> correctOptionCol = new TableColumn<>("Selected answer");
        correctOptionCol.setCellValueFactory( cell -> new ReadOnlyStringWrapper(cell.getValue().getAnswer()));
        TableColumn<AnswerQuestionData, String> isCorrectCol = new TableColumn<>("Answered Correctly");
        isCorrectCol.setCellValueFactory( cell -> new ReadOnlyStringWrapper(cell.getValue().getCorrect()));
        TableColumn<AnswerQuestionData, String> endDate = new TableColumn<>("Date Expired");
        endDate.setCellValueFactory( cell -> new ReadOnlyStringWrapper(cell.getValue().getEndDate()));

        answeredQuestionTable.getColumns().addAll(accessCodeCol,questionCol,correctOptionCol, isCorrectCol,endDate);

        VBox controls = new VBox(10, new Label("Answered Questions (Expired)"), answeredQuestionTable);
        controls.setPadding(new Insets(10));

        // Add search filters (DatePicker, Filter Button, etc.) here

        return new Tab("View Past Results", controls);
    }

    // --- Logic Methods ---

    private void handleEnterCode() {
        try {
            appManager.getQuizService().getQuestionByAccessCode(Integer.parseInt(accessCodeField.getText()));
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Access code must be a number.").showAndWait();
        }
    }

    private void handleLogout() {
        new Alert(Alert.AlertType.INFORMATION, "Logged out successfully.").showAndWait();
        app.showLoginView(this);
    }

    public Scene getScene() {
        return scene;
    }

    private void onShowQuestionResponse(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            if (evt.getNewValue() instanceof ShowQuestionOptionsResponse response) {
                if (response.exist()) {
                    app.showAnswerQuestionView(this);
                } else {
                    new Alert(Alert.AlertType.ERROR, response.message()).showAndWait();
                }
            }
        });
    }

    private void onShowAnswerResponse(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            if (evt.getNewValue() instanceof ShowQuestionsAnswerResponse response) {
                if (response.success()) {
                    answeredQuestionsList = appManager.getQuizService().getAnswersDTOS();
                    ObservableList<AnswerQuestionData> questions = FXCollections.observableArrayList(answeredQuestionsList
                            .stream().map(AnswerQuestionData::fromAnswerQuestionDto).toList());
                    answeredQuestionTable.setItems(questions);
                } else {
                    new Alert(Alert.AlertType.ERROR, response.message()).showAndWait();
                }
            }
        });
    }

    private void onTabChangeListener(ObservableValue<? extends Tab> obs, Tab oldTab, Tab newTab) {
        if (newTab.getText().equals("View Past Results")) {
            appManager.getQuizService().showQuestionsAnswers();
        }
    }
}
