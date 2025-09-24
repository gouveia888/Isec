package pt.isec.pa.drawing.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Drawing implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Figure.FigureType currentType;
    private double r,g,b;
    private ArrayList<Figure> figures;
    private Figure current;

    public Drawing() {
        figures = new ArrayList<>();
        currentType = Figure.FigureType.LINE;
        r=g=b=0;
    }

    public double getR() {
        return r;
    }
    public double getG() {
        return g;
    }
    public double getB() {
        return b;
    }
    public void setRGB(double r,double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Figure.FigureType getCurrentType() {
        return currentType;
    }

    public void setCurrentType(Figure.FigureType currentType) {
        this.currentType = currentType;
    }

    public void createFigure(double x, double y) {
        current = currentType.createFigure();
        current.setP1(x,y);
        current.setP2(x,y);
        current.setRGB(r,g,b);
    }

    public void updateCurrentFigure(double x, double y) {
        if(current==null)
            return;
        current.setP2(x,y);
    }

    public void finishCurrentFigure(double x,double y) {
        if(current==null)
            return;
        current.setP2(x,y);
        figures.add(current);
        current = null;
    }

    public Figure getCurrentFigure() {
        return current;
    }

    public List<Figure> getList() {
        List<Figure> retLst = new ArrayList<>();
        for(Figure fig : figures) {
            try {
                retLst.add(fig.clone());
            } catch (CloneNotSupportedException ignored) {
            }
        }
        return retLst;
    }

    public void clearAll() {
        figures.clear();
    }

    public void removeLast() {
        if (!figures.isEmpty())
            figures.removeLast();
    }

    public void remove(int selectedIndex) {
        if (selectedIndex>=0 && selectedIndex<=figures.size())
            figures.remove(selectedIndex);
    }
}
