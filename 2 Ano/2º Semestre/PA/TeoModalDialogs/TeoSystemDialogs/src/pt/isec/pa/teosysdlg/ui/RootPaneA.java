package pt.isec.pa.teosysdlg.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import pt.isec.pa.teosysdlg.model.ModelManager;

public class RootPaneA extends BorderPane {
    ModelManager data;

    Label label;
    Button button;

    public RootPaneA(ModelManager data) {
        this.data=data;

        createViews();
        registerHandlers();
        update();
    }
    private void createViews() {
        setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
        label = new Label("A");
        label.setTextFill(Color.INDIGO);
        setCenter(label);
        button = new Button("Decrement");
        button.setPrefWidth(9999);
        setBottom(button);
    }

    private void registerHandlers() {
        data.addListener(ModelManager.PROP_MODEL_DATA,
                evt -> update());
        button.setOnAction(e -> {
            data.setNumber(data.getNumber()-1);
        });
    }

    private void update() {
        label.setText("A: "+data.toString());
    }
}
