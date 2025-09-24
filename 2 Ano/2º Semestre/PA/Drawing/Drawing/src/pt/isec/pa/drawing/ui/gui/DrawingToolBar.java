package pt.isec.pa.drawing.ui.gui;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import pt.isec.pa.drawing.model.DrawingManager;
import pt.isec.pa.drawing.model.data.Figure;

public class DrawingToolBar extends ToolBar {
    private static final int TOGGLE_SIZE = 40;
    private static final int TOGGLE_IMG_SIZE = TOGGLE_SIZE-10;

    DrawingManager drawing;
    ToggleButton btnRed,btnGreen,btnBlue,btnOther;
    ToggleButton btnLine,btnRect,btnOval;
    Rectangle rectOther;
    ToggleGroup colorsGroup,figuresGroup;

    ColorPicker colorPicker;

    public DrawingToolBar(DrawingManager drawing) {
        this.drawing = drawing;

        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        Rectangle rectR = new Rectangle(0,0,TOGGLE_IMG_SIZE,TOGGLE_IMG_SIZE);
        rectR.setFill(Color.color(1,0,0));
        Rectangle rectG = new Rectangle(0,0,TOGGLE_IMG_SIZE,TOGGLE_IMG_SIZE);
        rectG.setFill(Color.color(0,1,0));
        Rectangle rectB = new Rectangle(0,0,TOGGLE_IMG_SIZE,TOGGLE_IMG_SIZE);
        rectB.setFill(Color.color(0,0,1));
        rectOther = new Rectangle(0,0,TOGGLE_IMG_SIZE,TOGGLE_IMG_SIZE);
        rectOther.setFill(Color.MAGENTA);

        btnRed   = new ToggleButton(null, rectR);
        btnRed.setPrefSize(TOGGLE_SIZE,TOGGLE_SIZE);
        btnGreen = new ToggleButton(null, rectG);
        btnRed.setPrefSize(TOGGLE_SIZE,TOGGLE_SIZE);
        btnBlue  = new ToggleButton(null, rectB);
        btnRed.setPrefSize(TOGGLE_SIZE,TOGGLE_SIZE);
        btnOther = new ToggleButton("Random", rectOther);
        btnRed.setPrefHeight(TOGGLE_SIZE);

        colorPicker = new ColorPicker();
        colorPicker.setPrefHeight(TOGGLE_SIZE);

        colorsGroup  = new ToggleGroup();
        btnRed.setToggleGroup(colorsGroup);
        btnGreen.setToggleGroup(colorsGroup);
        btnBlue.setToggleGroup(colorsGroup);
        btnOther.setToggleGroup(colorsGroup);

        Line shLine = new Line(0,0,TOGGLE_IMG_SIZE,TOGGLE_IMG_SIZE);
        shLine.setFill(Color.INDIGO);
        Rectangle shRect = new Rectangle(0,0,TOGGLE_IMG_SIZE,TOGGLE_IMG_SIZE);
        shRect.setFill(Color.INDIGO);
        Ellipse shOval = new Ellipse(0,0,TOGGLE_IMG_SIZE/2.0,TOGGLE_IMG_SIZE/2.0);
        shOval.setFill(Color.INDIGO);

        btnLine = new ToggleButton(null, shLine);
        btnLine.setPrefSize(TOGGLE_SIZE,TOGGLE_SIZE);
        btnRect = new ToggleButton(null, shRect);
        btnRect.setPrefSize(TOGGLE_SIZE,TOGGLE_SIZE);
        btnOval = new ToggleButton(null, shOval);
        btnOval.setPrefSize(TOGGLE_SIZE,TOGGLE_SIZE);

        figuresGroup = new ToggleGroup();
        btnLine.setToggleGroup(figuresGroup);
        btnRect.setToggleGroup(figuresGroup);
        btnOval.setToggleGroup(figuresGroup);

        this.getItems().addAll(btnRed,btnGreen,btnBlue,btnOther,colorPicker,new Separator(),btnLine,btnRect,btnOval);
    }

    private void registerHandlers() {
        btnRed.setOnAction(actionEvent -> {
            drawing.setRGB(1,0,0);
            update();
        });
        btnGreen.setOnAction(actionEvent -> {
            drawing.setRGB(0,1,0);
            update();
        });
        btnBlue.setOnAction(actionEvent -> {
            drawing.setRGB(0,0,1);
            update();
        });
        btnOther.setOnAction(actionEvent -> {
            drawing.setRGB(Math.random(),Math.random(),Math.random());
            update();
        });
        colorPicker.setOnAction(actionEvent -> {
            Color color = colorPicker.getValue();
            drawing.setRGB(color.getRed(),color.getGreen(),color.getBlue());
            update();
        });
        btnRed.fire();

        btnLine.setOnAction(actionEvent -> {
            drawing.setCurrentType(Figure.FigureType.LINE);
            update();
        });
        btnRect.setOnAction(actionEvent -> {
            drawing.setCurrentType(Figure.FigureType.RECTANGLE);
            update();
        });
        btnOval.setOnAction(actionEvent -> {
            drawing.setCurrentType(Figure.FigureType.OVAL);
            update();
        });
        btnLine.fire();
    }

    private void update() {
        Color color = Color.color(drawing.getR(),drawing.getG(),drawing.getB());
        rectOther.setFill(color);
        colorPicker.setValue(color);

        if (drawing.getR()==1 && drawing.getG()==0 && drawing.getB()==0)
            btnRed.setSelected(true);
        else if (drawing.getR()==0 && drawing.getG()==1 && drawing.getB()==0)
            btnGreen.setSelected(true);
        else if (drawing.getR()==0 && drawing.getG()==0 && drawing.getB()==1)
            btnBlue.setSelected(true);
        else
            btnOther.setSelected(true);

        switch (drawing.getCurrentType()) {
            case LINE -> btnLine.setSelected(true);
            case RECTANGLE -> btnRect.setSelected(true);
            case OVAL -> btnOval.setSelected(true);
        }
    }
}
