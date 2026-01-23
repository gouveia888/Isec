package ui.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.QuizAppManager;
import network.response.RegisterUserResponse;
import ui.QuizApp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RegisterView
    implements IClosableView
{
    private QuizApp app;
    private QuizAppManager appManager;

    private Scene scene;
    private Stage stage;

    private VBox roleFieldsBox;
    private TextField emailField;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField instructorCodeField;
    private TextField studentNumberField;
    private HBox roleBox;
    private ToggleGroup roleGroup;
    private RadioButton instructorRadio;
    private RadioButton studentRadio;
    private Button submitButton;
    private Button cancelButton;

    private PropertyChangeListener registerUserResponseListener = this::onRegisterUserResponse;

    public RegisterView(QuizApp app, QuizAppManager appManager) {
        this.app = app;
        this.appManager = appManager;


        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");

        // --- Common fields ---
        emailField = new TextField();
        emailField.setPromptText("Email");

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // --- Instructor fields ---
        instructorCodeField = new TextField();
        instructorCodeField.setPromptText("Instructor Code");

        // --- Student fields ---
        studentNumberField = new TextField();
        studentNumberField.setPromptText("Student Number");

        // Role selector
        roleGroup = new ToggleGroup();
        instructorRadio = new RadioButton("Instructor");
        studentRadio = new RadioButton("Student");
        instructorRadio.setToggleGroup(roleGroup);
        studentRadio.setToggleGroup(roleGroup);

        roleBox = new HBox(10, instructorRadio, studentRadio);
        roleBox.setAlignment(Pos.CENTER);

        // Container for role-specific fields
        roleFieldsBox = new VBox(10);
        roleFieldsBox.setAlignment(Pos.CENTER_LEFT);

        submitButton = new Button("Register");
        cancelButton = new Button("Cancel");


        VBox layout = new VBox(12,
                new Label("Register New User"),
                emailField,
                usernameField,
                passwordField,
                roleBox,
                roleFieldsBox,
                submitButton,
                cancelButton
        );

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        this.scene = new Scene(layout, 350, 350);
    }

    private void registerHandlers() {
        // Change fields based on selected role
        roleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            roleFieldsBox.getChildren().clear();
            if (newVal == instructorRadio) {
                roleFieldsBox.getChildren().add(instructorCodeField);
            } else if (newVal == studentRadio) {
                roleFieldsBox.getChildren().add(studentNumberField);
            }
        });

        // Submit button
        submitButton.setOnAction(this::onSubmitPressed);
        cancelButton.setOnAction( _ -> app.showLoginView(this));

        // submit response
        appManager.addPropertyChangeListener(QuizAppManager.REGISTER_USER_RESPONSE, registerUserResponseListener);
    }

    @Override
    public void unregisterHandlers() {
        appManager.removePropertyChangeListener(QuizAppManager.REGISTER_USER_RESPONSE, registerUserResponseListener);
    }

    private void update() {

    }

    public Scene getScene() {
        return scene;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void onRegisterUserResponse(PropertyChangeEvent packet) {
        Platform.runLater(() -> {
            if (packet.getNewValue() instanceof RegisterUserResponse response) {
                if (response.success()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, response.message(), ButtonType.OK);
                    alert.showAndWait();
                    app.showLoginView(this);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, response.message(), ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }

    private void onSubmitPressed(ActionEvent e) {
        if (roleGroup.getSelectedToggle() == null) {
            showError("Please select Instructor or Student.");
            return;
        }

        if (emailField.getText().isEmpty() ||
            usernameField.getText().isEmpty() ||
            passwordField.getText().isEmpty()) {
            showError("Email, username, and password are required.");
            return;
        }

        if (instructorRadio.isSelected()) {
            if (instructorCodeField.getText().isEmpty()) {
                showError("Instructor code required.");
                return;
            }
            appManager.getAuthenticatorService().registerInstructor(
                    emailField.getText(),
                    passwordField.getText(),
                    usernameField.getText(),
                    instructorCodeField.getText()
            );
        }

        if (studentRadio.isSelected()) {
            if (studentNumberField.getText().isEmpty()) {
                showError("Student number required.");
                return;
            }
            appManager.getAuthenticatorService().registerStudent(
                    emailField.getText(),
                    passwordField.getText(),
                    usernameField.getText(),
                    studentNumberField.getText()
            );
        }
    }
}
