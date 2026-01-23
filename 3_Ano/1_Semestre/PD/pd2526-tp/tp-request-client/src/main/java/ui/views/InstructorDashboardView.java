package ui.views;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AuthenticatorService;
import model.QuizAppManager;
import network.data.QuestionDTO;
import model.QuizService;
import network.data.QuestionStatisticsDTO;
import network.data.StudentAnswerDTO;
import network.enums.QuestionFilter;
import network.response.CreateQuestionResponse;
import network.response.DeleteQuestionResponse;
import network.response.EditQuestionResponse;
import network.response.ViewQuestionStatisticsResponse;
import ui.QuizApp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class InstructorDashboardView
    implements IClosableView
{

    // manage
    private QuizApp app;
    private QuizAppManager appManager;
    private AuthenticatorService authenticatorService;
    private QuizService quizService;

    // visual state
    private boolean isEdit = false;
    private int editQuestionId = -1;
    private ObservableList<QuestionDTO> questionDTOList;


    // ui
    private Scene scene;
    private TableView<QuestionDTO> questionTable = new TableView<>();
    private VBox optionInputContainer = new VBox(5);
    private Spinner<Integer> optionCountSpinner = new Spinner<>(2, 6, 4);
    private TextField statementField = new TextField();
    //private TextField correctOptionIndexField = new TextField();
    private Spinner<Integer> correctOptionSpinner = new Spinner<>(1, 6, 1);
    private DatePicker startDatePicker = new DatePicker();
    private TextField startTimeField = new TextField("00:00");
    private DatePicker endDatePicker = new DatePicker();
    private TextField endTimeField = new TextField("23:59");
    private ComboBox<QuestionFilter> statusFilterComboBox = new ComboBox<>(FXCollections.observableArrayList(QuestionFilter.values()));
    private TitledPane creationPane;
    private Button submitQuestionButton;

    private PropertyChangeListener createQuestionResponseListener = this::onCreateQuestionResponse;
    private PropertyChangeListener editQuestionResponseListener = this::onEditQuestionResponse;
    private PropertyChangeListener deleteQuestionResponseListener = this::onDeleteQuestionResponse;
    private PropertyChangeListener listInstructorQuestionResponseListener = this::onListInstructorQuestionResponse;
    private PropertyChangeListener viewQuestionStatisticsResponseListener = this::onViewQuestionStatisticsResponse;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public InstructorDashboardView(QuizApp app, QuizAppManager quizAppManager) {
        this.app = app;
        this.appManager = quizAppManager;
        this.authenticatorService = quizAppManager.getAuthenticatorService();
        this.quizService = quizAppManager.getQuizService();


        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        // --- Setup ---
        statusFilterComboBox.setValue(QuestionFilter.ALL);
        statusFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> requestQuestions(newVal));
        optionCountSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateOptionFields(newValue));
        updateOptionFields(4);

        // --- Layout ---
        BorderPane root = new BorderPane();

        root.setTop(createHeader());
        root.setCenter(createQuestionManagementPanel());
        root.setRight(createQuestionCreationPanel());

        this.scene = new Scene(root, 1200, 750);
    }

    private void registerHandlers() {
        appManager.addPropertyChangeListener(QuizAppManager.CREATE_QUESTION_RESPONSE, createQuestionResponseListener);
        appManager.addPropertyChangeListener(QuizAppManager.EDIT_QUESTION_RESPONSE, editQuestionResponseListener);
        appManager.addPropertyChangeListener(QuizAppManager.DELETE_QUESTION_RESPONSE, deleteQuestionResponseListener);
        appManager.addPropertyChangeListener(QuizAppManager.LIST_INSTRUCTOR_QUESTIONS_RESPONSE, listInstructorQuestionResponseListener);
        appManager.addPropertyChangeListener(QuizAppManager.VIEW_QUESTION_STATISTICS_RESPONSE, viewQuestionStatisticsResponseListener);
    }

    @Override
    public void unregisterHandlers() {
        appManager.removePropertyChangeListener(QuizAppManager.CREATE_QUESTION_RESPONSE, createQuestionResponseListener);
        appManager.removePropertyChangeListener(QuizAppManager.EDIT_QUESTION_RESPONSE, editQuestionResponseListener);
        appManager.removePropertyChangeListener(QuizAppManager.DELETE_QUESTION_RESPONSE, deleteQuestionResponseListener);
        appManager.removePropertyChangeListener(QuizAppManager.LIST_INSTRUCTOR_QUESTIONS_RESPONSE, listInstructorQuestionResponseListener);
        appManager.removePropertyChangeListener(QuizAppManager.VIEW_QUESTION_STATISTICS_RESPONSE, viewQuestionStatisticsResponseListener);
    }

    private void update() {
        requestQuestions(QuestionFilter.ALL);
        loadQuestions();
    }


    // --- Component Creation Methods ---

    private VBox createHeader() {
        Label welcomeLabel = new Label("Welcome, " + authenticatorService.getUserName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        Button editUserButton = new Button("Edit Account Details");
        editUserButton.setOnAction(e -> app.showEditUserView(this));

        HBox header = new HBox(10, welcomeLabel, new Region(), logoutButton, editUserButton);
        HBox.setHgrow(new Region(), Priority.ALWAYS);
        header.setPadding(new Insets(10));

        return new VBox(header, new Separator());
    }

    private TitledPane createQuestionCreationPanel() {
        // Form layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        int row = 0;
        formGrid.add(new Label("Statement:"), 0, row);
        formGrid.add(statementField, 1, row++);

        formGrid.add(new Label("No. Options:"), 0, row);
        formGrid.add(optionCountSpinner, 1, row++);

        // Options container is dynamic
        formGrid.add(new Label("Options:"), 0, row);
        formGrid.add(optionInputContainer, 1, row++);

        formGrid.add(new Label("Correct Index (1-N):"), 0, row);
        formGrid.add(correctOptionSpinner, 1, row++);
        //formGrid.add(correctOptionIndexField, 1, row++);

        formGrid.add(new Label("Start Date/Time:"), 0, row);
        formGrid.add(new HBox(5, startDatePicker, startTimeField), 1, row++);
        startDatePicker.setValue(LocalDate.now());

        formGrid.add(new Label("End Date/Time:"), 0, row);
        formGrid.add(new HBox(5, endDatePicker, endTimeField), 1, row++);
        endDatePicker.setValue(LocalDate.now());

        submitQuestionButton = new Button("Create Question");
        submitQuestionButton.setMaxWidth(Double.MAX_VALUE);
        submitQuestionButton.setOnAction(e -> handleCreateQuestion());
        formGrid.add(submitQuestionButton, 0, row, 2, 1);

        creationPane = new TitledPane("New Question Creation", formGrid);
        creationPane.setCollapsible(false);
        return creationPane;
    }

    private VBox createQuestionManagementPanel() {
        // Table setup
        TableColumn<QuestionDTO, String> statementCol = new TableColumn<>("Statement");
        statementCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatement()));
        TableColumn<QuestionDTO, String> accessCodeCol = new TableColumn<>("Access Code");
        accessCodeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty("" + cellData.getValue().getQuestionId()));
        TableColumn<QuestionDTO, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getState().toString()));
        TableColumn<QuestionDTO, String> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStartDate().toString()));
        TableColumn<QuestionDTO, String> endDateCol = new TableColumn<>("End Date");
        endDateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEndDate().toString()));


        questionTable.getColumns().addAll(statementCol, accessCodeCol, statusCol, startDateCol, endDateCol);
        questionDTOList = FXCollections.observableArrayList();
        questionTable.setItems(questionDTOList);

        // Buttons
        Button createButton = new Button("Create");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button viewResultsButton = new Button("View Results");

        createButton.setOnAction(e -> handleCreateButton());
        editButton.setOnAction(e -> handleEditQuestion());
        deleteButton.setOnAction(e -> handleDeleteQuestion());
        viewResultsButton.setOnAction(e -> handleViewResults());

        HBox buttonBar = new HBox(10, createButton, editButton, deleteButton, viewResultsButton);

        // Filtering
        HBox filterBox = new HBox(10, new Label("Filter by Status:"), statusFilterComboBox);
        filterBox.setAlignment(Pos.CENTER_LEFT);

//        // Disable Edit/Delete initially and link to table selection
        editButton.disableProperty().bind(
                questionTable.getSelectionModel().selectedItemProperty().isNull()
        );
        deleteButton.disableProperty().bind(editButton.disableProperty());
        viewResultsButton.disableProperty().bind(editButton.disabledProperty());

//        viewResultsButton.disableProperty().bind(questionTable.getSelectionModel().selectedItemProperty().isNull().or(
//                questionTable.getSelectionModel().selectedItemProperty().map(p -> p.getState() == QuestionFilter.EXPIRED)
//        ));
        viewResultsButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> {
                            var selected = questionTable.getSelectionModel().getSelectedItem();
                            return selected == null || selected.getState() != QuestionFilter.EXPIRED;
                        },
                        questionTable.getSelectionModel().selectedItemProperty()
                )
        );


        VBox managementBox = new VBox(10, filterBox, questionTable, buttonBar);
        managementBox.setPadding(new Insets(10));
        VBox.setVgrow(questionTable, Priority.ALWAYS);
        return managementBox;
    }

    private void handleCreateButton() {
        isEdit = false;
        updateForm(null);
    }


    // --- Logic Methods ---

    private void updateOptionFields(int count) {
        optionInputContainer.getChildren().clear();
        for (int i = 1; i <= count; i++) {
            TextField optionField = new TextField();
            optionField.setPromptText("Option " + i);
            optionField.setId("option" + i + "Field"); // Used for lookup
            optionInputContainer.getChildren().add(optionField);
        }
    }

    private void requestQuestions() {
        appManager.getQuizService().getInstructorQuestions(QuestionFilter.ALL);
    }

    private void requestQuestions(QuestionFilter filter) {
        appManager.getQuizService().getInstructorQuestions(filter);
    }

    private void loadQuestions() {
        questionDTOList.clear();
        questionDTOList.addAll(appManager.getQuizService().getQuestions());
    }

    private void handleCreateQuestion() {
        try {
            // retrieve data
            String statement = statementField.getText();
            //int correctOption = Integer.parseInt(correctOptionIndexField.getText());
            int correctOption = correctOptionSpinner.getValue();
            LocalTime startTime = LocalTime.parse(startTimeField.getText(), timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeField.getText(), timeFormatter);
            LocalDateTime start = startDatePicker.getValue().atTime(startTime);
            LocalDateTime end = endDatePicker.getValue().atTime(endTime);
            Map<Integer, String> options = new HashMap<>();
            for (int i = 1; i <= optionCountSpinner.getValue(); i++) {
                // Dynamically look up the generated text fields
                TextField optionField = (TextField) optionInputContainer.lookup("#option" + i + "Field");
                options.put(i, optionField.getText());
            }

            // validate
            if (statement.isBlank()) {
                new Alert(Alert.AlertType.ERROR, "Statement is mandatory").showAndWait();
                return;
            }
            if (correctOption < 1 || correctOption > options.size()) {
                new Alert(Alert.AlertType.ERROR, "Correct Index must be a value between 1 and %d".formatted(options.size())).showAndWait();
                return;

            }
            if (!optionsValid(options)) {
                new Alert(Alert.AlertType.ERROR, "Options can't be empty").showAndWait();
                return;
            }
            if (start.isAfter(end)) {
                new Alert(Alert.AlertType.ERROR, "End time must be after the start time").showAndWait();
                return;
            }


            if (isEdit) {
                quizService.editQuestion(editQuestionId, statement, options.values().stream().toList(), correctOption, start, end);
            } else {
                quizService.createQuestion(statement, options.values().stream().toList(), correctOption, start, end);
            }
        } catch (NumberFormatException _) {
            new Alert(Alert.AlertType.ERROR, "Correct Index must be a value between 1 and %d".formatted(optionCountSpinner.getValue())).showAndWait();
        } catch (DateTimeParseException _) {
            new Alert(Alert.AlertType.ERROR, "Time must be in the HH:mm formated, and the date int the YY/MM/dd format").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Input Error: Ensure all fields are correctly filled, including time (HH:mm) and index.").showAndWait();
        }
    }

    private boolean optionsValid(Map<Integer, String> options) {
        for (var value : options.values()) {
            if (value.isBlank())
                return false;
        }
        return true;
    }

    private void handleEditQuestion() {
        QuestionDTO selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            editQuestionId = selected.getQuestionId();
            isEdit = true;
            updateForm(selected);
        }
    }

    private void handleDeleteQuestion() {
        QuestionDTO selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this question?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    quizService.deleteQuestion(selected.getQuestionId());
                }
            });
        }
    }

    private void handleViewResults() {
        QuestionDTO selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected != null && selected.isExpired()) {
            quizService.viewQuestionStatistics(selected.getQuestionId());
        } else {
            new Alert(Alert.AlertType.ERROR, "Can't view results for non expired questions. ").showAndWait();
        }
    }

    private void handleLogout() {
        new Alert(Alert.AlertType.INFORMATION, "Logged out successfully.").showAndWait();
        app.showLoginView(this);
    }

    public Scene getScene() {
        return scene;
    }

    private void onListInstructorQuestionResponse(PropertyChangeEvent evt) {
        loadQuestions();
    }

    private void updateForm(QuestionDTO questionDTO) {
        String formTitle = isEdit ? "Edit Question" : "New Question Creation";
        String submitTitle = isEdit ? "Update Question" : "Create Question";

        // If null cleanup
        if (questionDTO == null) {
            statementField.setText("");
            //updateOptionFields(4);
            optionCountSpinner.getValueFactory().setValue(4);
            for (Node node : optionInputContainer.getChildren()) {
                if (node instanceof TextField textField) {
                        textField.setText("");
                }
            }
            correctOptionSpinner.getValueFactory().setValue(1);
            startDatePicker.setValue(LocalDate.now());
            endDatePicker.setValue(LocalDate.now());
            startTimeField.setText("00:00");
            endTimeField.setText("23:59");
        }
        // otherwise use the provided values
        else {
            statementField.setText(questionDTO.getStatement());
            //updateOptionFields(questionDTO.getOptions().size());
            optionCountSpinner.getValueFactory().setValue(questionDTO.getOptions().size());
            Iterator<String> optionIt = questionDTO.getOptions().iterator();
            for (Node node : optionInputContainer.getChildren()) {
                if (node instanceof TextField textField) {
                    if (optionIt.hasNext())
                        textField.setText(optionIt.next());
                }
            }
            correctOptionSpinner.getValueFactory().setValue(questionDTO.getCorrectOption() + 1);
            startDatePicker.setValue(questionDTO.getStartDate().toLocalDate());
            endDatePicker.setValue(questionDTO.getEndDate().toLocalDate());
            startTimeField.setText(questionDTO.getStartDate().toLocalTime().format(timeFormatter));
            endTimeField.setText(questionDTO.getEndDate().toLocalTime().format(timeFormatter));
        }

        creationPane.setText(formTitle);
        submitQuestionButton.setText(submitTitle);
    }

    private void onCreateQuestionResponse(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            if (evt.getNewValue() instanceof CreateQuestionResponse response) {
                if (response.success()) {
                    isEdit = false;
                    updateForm(null);

                    requestQuestions();
                    new Alert(Alert.AlertType.INFORMATION, response.message()).showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, response.message()).showAndWait();
                }
            }
        });
    }

    private void onEditQuestionResponse(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            if (evt.getNewValue() instanceof EditQuestionResponse response) {
                if (response.success()) {
                    isEdit = false;
                    updateForm(null);

                    requestQuestions();
                    new Alert(Alert.AlertType.INFORMATION, response.message()).showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, response.message()).showAndWait();
                }
            }
        });
    }

    private void onDeleteQuestionResponse(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            if (evt.getNewValue() instanceof DeleteQuestionResponse response) {
                if (response.success()) {
                    isEdit = false;
                    updateForm(null);

                    requestQuestions();
                    new Alert(Alert.AlertType.INFORMATION, response.message()).showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, response.message()).showAndWait();
                }
            }
        });
    }

    private void onViewQuestionStatisticsResponse(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            if (evt.getNewValue() instanceof ViewQuestionStatisticsResponse response) {
                if (response.success()) {
                    viewStatistics(response.questionStatisticsDTO());
                } else {
                    new Alert(Alert.AlertType.ERROR, response.message()).showAndWait();
                }
            }
        });
    }

    private void viewStatistics(QuestionStatisticsDTO statisticsDTO) {
        Parent ui = new QuestionStatisticsView().createView(statisticsDTO);

        Stage dialog = new Stage();
        dialog.setTitle("Question Statistics");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);

        Scene newscene = new Scene(ui, 800, 600);
        dialog.setScene(newscene);

        dialog.showAndWait();
    }
}