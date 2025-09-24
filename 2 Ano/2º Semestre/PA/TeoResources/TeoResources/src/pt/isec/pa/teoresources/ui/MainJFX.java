package pt.isec.pa.teoresources.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.teoresources.Main;
import pt.isec.pa.teoresources.model.ModelData;

public class MainJFX extends Application {
    ModelData model;

    @Override
    public void init() throws Exception {
        super.init();
        model = Main.model;
    }

    @Override
    public void start(Stage stage) throws Exception {
        RootPane root = new RootPane(model);
        Scene scene = new Scene(root,800,600);
        //scene.setUserData(model);
        stage.setScene(scene);
        stage.setTitle("PA-DEIS-ISEC");
        stage.show();
    }
}
