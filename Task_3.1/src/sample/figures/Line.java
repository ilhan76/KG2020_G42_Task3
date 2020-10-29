package sample.figures;


import javafx.scene.image.PixelWriter;
import sample.Drawers.DDALineDrawer;
import sample.Drawers.LineDrawer;
import sample.tools.ScreenConvertor;
import sample.points.RealPoint;
import sample.points.ScreenPoint;

import java.util.ArrayList;

public class Line implements IFigure{
    private ArrayList<ScreenPoint> points;
    private RealPoint p1, p2;

    public Line(RealPoint p1, RealPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Line(double x1, double y1, double x2, double y2) {
        p1 = new RealPoint(x1, y1);
        p2 = new RealPoint(x2, y2);
    }

    public void setP1(RealPoint p1) {
        this.p1 = p1;
    }
    public void setP2(RealPoint p2) {
        this.p2 = p2;
    }

    public RealPoint getP1() {
        return p1;
    }
    public RealPoint getP2() {
        return p2;
    }
    public ArrayList<ScreenPoint> getPoints(){return points;}
    public void setPoints(ArrayList<ScreenPoint> points){
        this.points = points;
    }

    @Override
    public void draw(ScreenConvertor screenConvertor, PixelWriter pixelWriter) {
        LineDrawer ld = new DDALineDrawer(pixelWriter);
        ld.drawLine(screenConvertor.realToScreen(p1), screenConvertor.realToScreen(p2));
        setPoints(ld.getPoints());
    }

    @Override
    public ArrayList<RealPoint> getVertices() {
        return null;
    }
}
