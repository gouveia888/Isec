package pt.isec.pa.e10res.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.e10res.model.ModelData;
import pt.isec.pa.e10res.ui.res.ImageManager;

import javax.swing.*;

public class RootPane extends BorderPane {
    ModelData data;

    Pane top,bottom,left,right,center;
    Canvas canvas;
    double xi,yi,xf,yf;

    public RootPane(ModelData data) {
        this.data=data;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        top = new Pane();
        top.setPrefHeight(50);
        top.setStyle("-fx-background-color: #80c0ff;");
        bottom = new Pane();
        bottom.setPrefHeight(50);
        bottom.setStyle("-fx-background-color: #80c0ff;");
        left = new Pane();
        left.setPrefWidth(50);
        left.setStyle("-fx-background-color: #80ffc0;");
        right = new Pane();
        right.setPrefWidth(50);
        right.setStyle("-fx-background-color: #80ffc0;");
        center = new Pane();
        //ScrollPane center = new ScrollPane();
        center.setStyle("-fx-background-color: #ffc080;");

        setTop(top);
        setBottom(bottom);
        setLeft(left);
        setRight(right);
        setCenter(center);

        canvas = new Canvas(5000,5000);
        //center.setContent(canvas);
        center.getChildren().add(canvas);
    }

    private void registerHandlers() {
        center.widthProperty().addListener(
                (_,_,_) -> {
                    canvas.setWidth(center.getWidth());
                    canvas.setHeight(center.getHeight());
                    update();
                });
        center.heightProperty().addListener(
                (_,_,_) -> {
                    canvas.setWidth(center.getWidth());
                    canvas.setHeight(center.getHeight());
                    update();
                });
        canvas.setOnMousePressed(mouseEvent -> {
            xi = xf;
            yi = yf;
            xf = mouseEvent.getX();
            yf = mouseEvent.getY();
            update();
        });
    }

    private void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LEMONCHIFFON);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

//        gc.strokeText(data.getMessage(),xi,yi);
//        gc.setTextAlign(TextAlignment.CENTER);
//        gc.setTextBaseline(VPos.CENTER);
        //gc.strokeLine(xi,yi,xf,yf);
        Color color = Color.color(Math.random(),Math.random(),Math.random());
        gc.setFill(color);
        gc.fillRect(Math.min(xi,xf),Math.min(yi,yf),Math.abs(xf-xi),Math.abs(yf-yi));
        gc.setStroke(color.darker());
        gc.setLineWidth(10);
        gc.strokeRect(Math.min(xi,xf),Math.min(yi,yf),Math.abs(xf-xi),Math.abs(yf-yi));

        gc.drawImage(
                ImageManager.getImage("img.png"),
                Math.min(xi,xf)+10,Math.min(yi,yf)+10,
                Math.abs(xf-xi)-20,Math.abs(yf-yi)-20);


    }
}