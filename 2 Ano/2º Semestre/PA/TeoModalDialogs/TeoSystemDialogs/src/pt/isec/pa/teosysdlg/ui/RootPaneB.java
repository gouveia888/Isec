package pt.isec.pa.teosysdlg.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import pt.isec.pa.teosysdlg.model.ModelManager;

public class RootPaneB extends BorderPane {
    ModelManager data;

    Label label;
    Button button;

    public RootPaneB(ModelManager data) {
        this.data=data;

        createViews();
        registerHandlers();
        update();
    }
    private void createViews() {
        setBackground(new Background(new BackgroundFill(Color.INDIGO, null, null)));
        label = new Label("B");
        label.setTextFill(Color.ORANGE);
        setCenter(label);
        button = new Button("Increment");
        button.setPrefWidth(9999);
        setBottom(button);
    }

    private void registerHandlers() {
        data.addListener(ModelManager.PROP_MODEL_DATA, evt -> update());
        button.setOnAction(e -> {
            data.setNumber(data.getNumber()+1);
        });
    }

    private void update() {
        label.setText("B: "+data.toString());
    }
}
