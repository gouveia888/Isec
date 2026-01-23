package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.QuizAppManager;

public class ShutdownMessageView {

    // managers
    private final QuizApp app;
    private final QuizAppManager appManager;
    private final String message;

    // ui
    private Label messageLabel;
    private Button loginButton;
    private Scene scene;


    public ShutdownMessageView(QuizApp app, QuizAppManager appManager, String message) {
        this.app = app;
        this.appManager = appManager;
        this.message = message;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        messageLabel = new Label(message);
        loginButton = new Button("Ok");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(messageLabel, loginButton);

        this.scene = new Scene(layout, 300, 100);
    }

    private void registerHandlers() {
        loginButton.setOnAction(_ -> {
            try {
                app.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void update() {
    }

    public Scene getScene() {
        return scene;
    }
}
