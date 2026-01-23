package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.QuizAppManager;
import ui.views.*;


public class QuizApp extends Application {

    private Stage primaryStage;
    private QuizAppManager quizAppManager;
    private boolean success = false;
    private String errorMessage;

    public QuizApp() {
    }

    @Override
    public void init() throws Exception {
        super.init();
        try {
            quizAppManager = new QuizAppManager(getParameters().getUnnamed());
            success = true;
        } catch (Exception e) {
            errorMessage = e.getMessage();
            success = false;
        }
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Quiz System");

        if (!success) {
            ErrorView.showError(errorMessage);
            return;
        }

        // listen to the shutdown message
        quizAppManager.addPropertyChangeListener(QuizAppManager.CLIENT_SHUTDOWN, evt -> {
            Platform.runLater(() -> showShutdownMessage(null, (String) evt.getNewValue()));
        });

        // Start with the Login View
        showLoginView(null);

        this.primaryStage.show();
    }

    // Method to switch the scene to the Login view
    public void showLoginView(IClosableView currentView) {
        if (currentView != null) {
            currentView.unregisterHandlers();
        }

        LoginView loginView = new LoginView(this, quizAppManager);
        primaryStage.setScene(loginView.getScene());
        primaryStage.setTitle("Quiz System Login");
    }

    // Method to switch the scene to the Register view
    public void showRegisterView(IClosableView currentView) {
        if (currentView != null) {
            currentView.unregisterHandlers();
        }

        RegisterView registerView = new RegisterView(this, quizAppManager);
        primaryStage.setScene(registerView.getScene());
        primaryStage.setTitle("Quiz System Login");
    }

    // Method to switch the scene to the Register view
    public void showEditUserView(IClosableView currentView) {
        if (currentView != null) {
            currentView.unregisterHandlers();
        }

        EditUserView editUserView = new EditUserView(this, quizAppManager);
        primaryStage.setScene(editUserView.getScene());
        primaryStage.setTitle("Edit User Details");
    }

    // Method to switch the scene to the Instructor Dashboard
    public void showInstructorDashboard(IClosableView currentView) {
        if (currentView != null) {
            currentView.unregisterHandlers();
        }

        InstructorDashboardView dashboard = new InstructorDashboardView(this, quizAppManager);
        primaryStage.setScene(dashboard.getScene());
        primaryStage.setTitle("Instructor Dashboard - " + quizAppManager.getAuthenticatorService().getUserName());
    }

    // Method to switch the scene to the Student Dashboard
    public void showStudentDashboard(IClosableView currentView) {
        if (currentView != null) {
            currentView.unregisterHandlers();
        }

        StudentDashboardView dashboard = new StudentDashboardView(this, quizAppManager);
        primaryStage.setScene(dashboard.getScene());
        primaryStage.setTitle("Student Dashboard - " + quizAppManager.getAuthenticatorService().getUserName());
    }

    public void showShutdownMessage(IClosableView currentView, String message) {
        if (currentView != null) {
            currentView.unregisterHandlers();
        }

        ShutdownMessageView view = new ShutdownMessageView(this, quizAppManager, message);
        primaryStage.setScene(view.getScene());
        primaryStage.setTitle("Shutdown");
    }


    public void showAnswerQuestionView(IClosableView currentView) {
        if (currentView != null) {
            currentView.unregisterHandlers();
        }

        AnswerQuestionView dashboard = new AnswerQuestionView(this, quizAppManager);
        primaryStage.setScene(dashboard.getScene());
        primaryStage.setTitle("Answer Question");
    }


    @Override
    public void stop() throws Exception {
        if (quizAppManager != null) {
            quizAppManager.shutdown();
        }
        super.stop();
        primaryStage.close();
    }
}