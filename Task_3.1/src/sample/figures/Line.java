package sample.figures;


import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sample.Drawers.DDALineDrawer;
import sample.Drawers.LineDrawer;
import sample.tools.ScreenConvertor;
import sample.points.RealPoint;
import sample.points.ScreenPoint;
import java.util.ArrayList;

public class Line implements IFigure {
    private ArrayList<ScreenPoint> points;
    private final ArrayList<RealPoint> vertices = new ArrayList<>();
    private RealPoint p1, p2;

    public Line(RealPoint p1, RealPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
        vertices.add(this.p1);
        vertices.add(this.p2);
    }

    public Line(double x1, double y1, double x2, double y2) {
        p1 = new RealPoint(x1, y1);
        p2 = new RealPoint(x2, y2);
    }

    public void setP1(RealPoint p1) {
        this.p1 = p1;
        vertices.clear();
        vertices.add(this.p1);
        vertices.add(this.p2);
    }
    public void setP2(RealPoint p2) {
        this.p2 = p2;
        vertices.clear();
        vertices.add(this.p1);
        vertices.add(this.p2);
    }
    public RealPoint getP1() {
        return p1;
    }
    public RealPoint getP2() {
        return p2;
    }

    public ArrayList<ScreenPoint> getPoints() {
        return points;
    }
    public void setPoints(ArrayList<ScreenPoint> points) {
        this.points = points;
    }
    @Override
    public ArrayList<Circle> drawFocus(ScreenConvertor screenConvertor, PixelWriter pixelWriter) {
        ArrayList<Circle> circles = new ArrayList<>();
        Circle c1 = new Circle(screenConvertor.r2s(p1).getX(), screenConvertor.r2s(p1).getY(), 15, Color.BLUE);
        Circle c2 = new Circle(screenConvertor.r2s(p2).getX(), screenConvertor.r2s(p2).getY(), 15, Color.BLUE);
        circles.add(c1);
        circles.add(c2);
        return circles;
    }
    @Override
    public void draw(ScreenConvertor screenConvertor, PixelWriter pixelWriter) {
        LineDrawer ld = new DDALineDrawer(pixelWriter);
        ld.drawLine(screenConvertor.realToScreen(p1), screenConvertor.realToScreen(p2));
        setPoints(ld.getPoints());
    }

    @Override
    public ArrayList<RealPoint> getVertices() {
        return vertices;
    }
}
