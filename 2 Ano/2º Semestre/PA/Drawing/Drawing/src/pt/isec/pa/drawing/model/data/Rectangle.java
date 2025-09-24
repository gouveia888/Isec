package pt.isec.pa.drawing.model.data;

public class Rectangle extends Figure {
    @Override
    public FigureType getType() {
        return FigureType.RECTANGLE;
    }

    @Override
    public double getX1() {
        return Math.min(x1, x2);
    }

    @Override
    public double getX2() {
        return Math.max(x1, x2);
    }

    @Override
    public double getY1() {
        return Math.min(y1, y2);
    }

    @Override
    public double getY2() {
        return Math.max(y1, y2);
    }
}
