package ui.views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.QuizAppManager;
import network.enums.AccountType;
import network.response.EditUserResponse;
import network.response.RegisterUserResponse;
import ui.QuizApp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditUserView
        implements IClosableView
{
    private QuizApp app;
    private QuizAppManager appManager;
    private AccountType accountType;

    private Scene scene;
    private Stage stage;

    private TextField emailField;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField studentNumberField;
    private Button submitButton;
    private Button cancelButton;

    private PropertyChangeListener editUserListener = this::onEditUserResponse;

    public EditUserView(QuizApp app, QuizAppManager appManager) {
        this.app = app;
        this.appManager = appManager;
        this.accountType = appManager.getAuthenticatorService().getAccountType();


        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit register");

        VBox layout = new VBox(12);

        // --- Common fields ---
        emailField = new TextField();
        emailField.setPromptText("Email");

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        layout.getChildren().addAll(
                new Label("Edit User"),
                emailField,
                usernameField,
                passwordField
        );

        // --- Student fields ---
        if(accountType == AccountType.STUDENT){
            studentNumberField = new TextField();
            studentNumberField.setPromptText("Student Number");
            layout.getChildren().add(studentNumberField);
        }

        submitButton = new Button("Submit");
        layout.getChildren().add(submitButton);

        cancelButton = new Button("Cancel");
        layout.getChildren().add(cancelButton);

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        this.scene = new Scene(layout, 350, 350);
    }

    private void registerHandlers() {

        // Submit button
        submitButton.setOnAction(_ -> {

            if (emailField.getText().isEmpty() ||
                usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty()) {
                showError("Email, username, and password are required.");
                return;
            }

            if (accountType == AccountType.INSTRUCTOR) {
                appManager.getAuthenticatorService().editInstructor(
                        emailField.getText(),
                        passwordField.getText(),
                        usernameField.getText()
                );
            }
            else {
                if (studentNumberField.getText().isEmpty()) {
                    showError("Student number required.");
                    return;
                }
                appManager.getAuthenticatorService().editStudent(
                        emailField.getText(),
                        passwordField.getText(),
                        usernameField.getText(),
                        studentNumberField.getText()
                );
            }
        });

        cancelButton.setOnAction(_ -> {
            if (accountType == AccountType.INSTRUCTOR) {
                app.showInstructorDashboard(this);
            }else{
                app.showStudentDashboard(this);
            }
        });


        // submit response
        appManager.addPropertyChangeListener(QuizAppManager.EDIT_USER_RESPONSE, editUserListener);
    }

    @Override
    public void unregisterHandlers() {
        appManager.removePropertyChangeListener(QuizAppManager.EDIT_USER_RESPONSE, editUserListener);
    }

    private void update() {
        // fill with default data
        usernameField.setText(appManager.getAuthenticatorService().getUserName());
        emailField.setText(appManager.getAuthenticatorService().getUserEmail());
        passwordField.setText(appManager.getAuthenticatorService().getUserPassword());
        if(appManager.getAuthenticatorService().getAccountType() == AccountType.STUDENT){
            studentNumberField.setText(appManager.getAuthenticatorService().getStudentNumber());
        }
    }

    public Scene getScene() {
        return scene;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void onEditUserResponse(PropertyChangeEvent packet) {
        Platform.runLater(() -> {
            if (packet.getNewValue() instanceof EditUserResponse response) {
                if (response.success()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, response.message(), ButtonType.OK);
                    alert.showAndWait();
                    if(accountType == AccountType.STUDENT){
                        app.showStudentDashboard(this);
                    }
                    else{
                        app.showInstructorDashboard(this);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, response.message(), ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }
}
