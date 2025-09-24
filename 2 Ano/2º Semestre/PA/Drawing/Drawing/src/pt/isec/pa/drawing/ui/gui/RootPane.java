package pt.isec.pa.drawing.ui.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pt.isec.pa.drawing.model.DrawingManager;

public class RootPane extends BorderPane {
    DrawingManager drawing;
    DrawingArea drawingArea;
    Pane areaPane;

    public RootPane(DrawingManager drawing) {
        this.drawing = drawing;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        setTop(
                new VBox(
                        new DrawingMenu(drawing,drawingArea),
                        new DrawingToolBar(drawing)
                )
        );

        drawingArea = new DrawingArea(drawing);
        areaPane = new Pane(drawingArea);
        setCenter(areaPane);
    }

    private void registerHandlers() {
        areaPane.widthProperty().addListener(observable -> drawingArea.updateSize(areaPane.getWidth(),areaPane.getHeight()));
        areaPane.heightProperty().addListener(observable -> drawingArea.updateSize(areaPane.getWidth(),areaPane.getHeight()));
    }

    private void update() {    }
}
