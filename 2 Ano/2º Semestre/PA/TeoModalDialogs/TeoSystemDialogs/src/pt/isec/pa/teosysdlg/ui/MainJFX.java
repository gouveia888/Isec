package pt.isec.pa.teosysdlg.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.teosysdlg.model.ModelManager;

public class MainJFX extends Application {
    private ModelManager manager;

    @Override
    public void init() throws Exception {
        manager = new ModelManager();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new RootPaneA(manager), 300, 200));
        stage.setTitle("Window A");
        stage.setX(100);
        stage.setY(100);
        stage.setOnCloseRequest(event -> {
            event.consume();
            if (confirmClose(stage)) {
                Platform.exit();
            }
        });
        stage.show();

        Stage stageB = new Stage();
        stageB.setScene(new Scene(new RootPaneB(manager), 300, 200));
        stageB.setTitle("Window B");
        stageB.setX(420);
        stageB.setY(100);
        stageB.setOnCloseRequest(event -> {
            event.consume();
            if (confirmClose(stageB)) {
                Platform.exit();
            }
        });
        stageB.show();
    }

    private boolean confirmClose(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Close Application");
        alert.setContentText("Are you sure you want to exit?");
        //alert.initModality(Modality.WINDOW_MODAL);
        //alert.initOwner(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(null);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
