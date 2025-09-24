package App;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class AppJFX extends Application {
    private final BatteryManager battery;

    public AppJFX() {
        battery = new BatteryManager();
    }

    @Override
    public void start(Stage stage) {
        RootPane root = new RootPane(battery);
        Scene scene = new Scene(root, 240, 200);

        stage.setTitle("Battery");
        stage.setScene(scene);
        stage.show();

        Stage stage2 = new Stage();
        RootPane root2 = new RootPane(battery);
        Scene scene2 = new Scene(root2, 240, 200);

        stage2.setTitle("Battery2");
        stage2.setScene(scene2);
        stage2.show();
    }
}
