package pt.isec.pa.teojfx.ui;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.isec.pa.teojfx.model.ModelData;

public class MyVHBoxPane extends HBox {
    ModelData data;
    // variables, including reference to views
    Canvas canvas;

    public MyVHBoxPane(ModelData data) {
        this.data = data;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setStyle("-fx-background-color: #ffffe0;");
        this.setPadding(new Insets(16));
        Pane p1 = new Pane();
        p1.setStyle("-fx-background-color: #ffc0c0;");
        p1.setPrefSize(9999,9999);
        Pane p2 = new Pane();
        p2.setStyle("-fx-background-color: #c0c0ff;");
        p2.setPrefSize(9999,9999);
        VBox vb1 = new VBox(p1,p2);

        canvas = new Canvas(100,100);
        Pane pc = new StackPane(canvas);
        pc.setStyle("-fx-background-color: #c0ffff;");
        pc.setPrefSize(9999,9999);
        pc.setPadding(new Insets(5));
        //pc.widthProperty().addListener((observableValue, number, t1) -> {canvas.setWidth(pc.getWidth()-20);canvas.setHeight(pc.getHeight()-20);update();});
        //pc.heightProperty().addListener((observableValue, number, t1) -> {canvas.setWidth(pc.getWidth()-20);canvas.setHeight(pc.getHeight()-20);update();});

        Pane p3 = new Pane();
        p3.setStyle("-fx-background-color: #ffc0ff;");
        p3.setPrefSize(9999,9999);
        Pane p4 = new Pane();
        p4.setStyle("-fx-background-color: #c0ffc0;");
        p4.setPrefSize(9999,9999);
        VBox vb2 = new VBox(p3,p4);

        this.getChildren().addAll(vb1,pc,vb2);
    }

    private void registerHandlers() {
    }

    private void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.ORANGE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setStroke(Color.INDIGO);
        gc.strokeLine(10,10,canvas.getWidth()-10,canvas.getHeight()-10);
        
    }

}
