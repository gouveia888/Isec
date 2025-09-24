package pt.isec.pa.drawing.ui.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pt.isec.pa.drawing.model.DrawingManager;
import pt.isec.pa.drawing.model.data.Figure;

public class DrawingArea extends Canvas {
    DrawingManager drawing;

    public DrawingArea(DrawingManager drawing) {
        super(500,500);
        this.drawing = drawing;

        registerHandlers();
        update();
    }

    private void registerHandlers() {
        this.setOnMousePressed(mouseEvent -> {
            drawing.createFigure(mouseEvent.getX(), mouseEvent.getY());
            update();
        });
        this.setOnMouseDragged(mouseEvent -> {
            drawing.updateCurrentFigure(mouseEvent.getX(), mouseEvent.getY());
            update();
        });
        this.setOnMouseReleased(mouseEvent -> {
            drawing.finishCurrentFigure(mouseEvent.getX(), mouseEvent.getY());
            update();
        });
    }

    public void update() {
        GraphicsContext gc = this.getGraphicsContext2D();

        clearScreen(gc);
        drawing.getList().forEach( figure -> drawFigure(gc,figure));
        drawFigure(gc,drawing.getCurrentFigure());
    }

    private void clearScreen(GraphicsContext gc) {
        gc.setFill(Color.LIGHTYELLOW);
        gc.fillRect(0,0,getWidth(),getHeight());
    }

    private void drawFigure(GraphicsContext gc, Figure figure) {
        if (figure == null) return;
        Color color = Color.color(figure.getR(),figure.getG(),figure.getB());
        gc.setFill(color);
        gc.setLineWidth(3);
        switch (figure.getType()) {
            case LINE -> {
                gc.setStroke(color);
                gc.strokeLine(figure.getX1(),figure.getY1(),figure.getX2(),figure.getY2());
            }
            case RECTANGLE -> {
                gc.fillRect(figure.getX1(),figure.getY1(),figure.getWidth(),figure.getHeight());
                gc.setStroke(color.darker());
                gc.strokeRect(figure.getX1(),figure.getY1(),figure.getWidth(),figure.getHeight());
            }
            case OVAL -> {
                gc.fillOval(figure.getX1(),figure.getY1(),figure.getWidth(),figure.getHeight());
                gc.setStroke(color.darker());
                gc.strokeOval(figure.getX1(),figure.getY1(),figure.getWidth(),figure.getHeight());
            }
        }
    }

    public void updateSize(double newWidth, double newHeight) {
        setWidth(newWidth);
        setHeight(newHeight);
        update();
    }
}

