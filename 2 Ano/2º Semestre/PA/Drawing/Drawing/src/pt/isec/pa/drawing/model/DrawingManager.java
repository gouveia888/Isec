package pt.isec.pa.drawing.model;

import pt.isec.pa.drawing.model.data.Drawing;
import pt.isec.pa.drawing.model.data.Figure;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.List;

public class DrawingManager {
    private Drawing drawing;

    public DrawingManager() {
        this.drawing = new Drawing();
    }

    /*public DrawingManager(Drawing drawing) {
        this.drawing = drawing;
    }*/

    public double getR() {
        return drawing.getR();
    }
    public double getG() {
        return drawing.getG();
    }
    public double getB() {
        return drawing.getB();
    }

    public void setRGB(double r,double g, double b) {
        drawing.setRGB(r, g, b);
    }

    public Figure.FigureType getCurrentType() {
        return drawing.getCurrentType();
    }

    public void setCurrentType(Figure.FigureType currentType) {
        drawing.setCurrentType(currentType);
    }

    public void createFigure(double x, double y) {
        drawing.createFigure(x,y);
    }

    public void updateCurrentFigure(double x, double y) {
        drawing.updateCurrentFigure(x, y);
    }

    public void finishCurrentFigure(double x,double y) {
        drawing.finishCurrentFigure(x, y);
    }

    public Figure getCurrentFigure() {
        return drawing.getCurrentFigure();
    }

    public List<Figure> getList() {
        return drawing.getList();
    }

    public void clearAll() {
        drawing.clearAll();
    }

    public void removeLast() {
        drawing.removeLast();
    }

    public void remove(int selectedIndex) {
        drawing.remove(selectedIndex);
    }

    public boolean load(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            drawing = (Drawing) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error loading drawing");
            return false;
        }
        return true;
    }

    public boolean save(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(drawing);
        } catch (Exception e) {
            System.err.println("Error writing drawing");
            return false;
        }
        return true;
    }
}