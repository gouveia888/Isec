package ui.views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.QuizAppManager;
import network.enums.AccountType;
import network.response.LoginUserResponse;
import network.response.RegisterUserResponse;
import ui.QuizApp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginView
    implements IClosableView
{
    // managers
    private final QuizApp app;
    private final QuizAppManager appManager;

    // ui
    private Scene scene;
    private TextField emailField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Label messageLabel = new Label();
    private Button loginButton;
    private Hyperlink registerInstructorLink;

    private final PropertyChangeListener registerUserListener;
    private final PropertyChangeListener loginUserListener;

    public LoginView(QuizApp app, QuizAppManager appManager) {
        this.app = app;
        this.appManager = appManager;

        registerUserListener = this::onRegisterUserResponse;
        loginUserListener = this::onLoginUserResponse;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        emailField.setPromptText("Email");
        passwordField.setPromptText("Password");

        loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        registerInstructorLink = new Hyperlink("Registration");

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                new Label("System Login"),
                emailField,
                passwordField,
                loginButton,
                messageLabel,
                new Separator(),
                registerInstructorLink
        );

        this.scene = new Scene(layout, 600, 400);
    }

    private void registerHandlers() {
        loginButton.setOnAction(_ -> requestLogin());
        registerInstructorLink.setOnAction(_ -> openRegisterWindow());

        appManager.addPropertyChangeListener(QuizAppManager.REGISTER_USER_RESPONSE, registerUserListener);
        appManager.addPropertyChangeListener(QuizAppManager.LOGIN_USER_RESPONSE, loginUserListener);
    }

    @Override
    public void unregisterHandlers() {
        appManager.removePropertyChangeListener(QuizAppManager.REGISTER_USER_RESPONSE, registerUserListener);
        appManager.removePropertyChangeListener(QuizAppManager.LOGIN_USER_RESPONSE, loginUserListener);
    }

    private void update() {

    }

    private void openRegisterWindow() {
        app.showRegisterView(this);
    }

    private void requestLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        messageLabel.setText("");
        appManager.getAuthenticatorService().authenticate(email, password);
    }

    public Scene getScene() {
        return scene;
    }

    private void onRegisterUserResponse(PropertyChangeEvent packet) {
        Platform.runLater(() -> {
            if (packet.getNewValue() instanceof RegisterUserResponse response) {
                if (!response.success()) {
                    messageLabel.setText(response.message());
                } else {
                    messageLabel.setText("Registered successfully!");
                }
            }
        });
    }

    private void onLoginUserResponse(PropertyChangeEvent packet) {
        Platform.runLater(() -> {
            if (packet.getNewValue() instanceof LoginUserResponse response) {
                if (!response.success()) {
                    messageLabel.setText("Invalid email or password.");
                } else {
                    unregisterHandlers();
                    if (response.accountType() == AccountType.INSTRUCTOR) {
                        app.showInstructorDashboard(this);
                    } else {
                        app.showStudentDashboard(this);
                    }
                }
            }
        });
    }
}
