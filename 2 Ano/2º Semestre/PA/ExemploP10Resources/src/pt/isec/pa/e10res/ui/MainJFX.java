package pt.isec.pa.e10res.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.e10res.model.ModelData;

import javax.swing.plaf.RootPaneUI;

public class MainJFX extends Application {
    ModelData model = new ModelData();

    @Override
    public void start(Stage stage) throws Exception {
        RootPane root = new RootPane(model);
        Scene scene = new Scene(root,800,600);
        stage.setScene(scene);
        stage.setTitle("Stage 4 - "+model.getMessage());
        stage.show();
    }
}
