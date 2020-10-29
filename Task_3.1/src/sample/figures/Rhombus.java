package sample.figures;

import javafx.scene.image.PixelWriter;
import sample.Drawers.PolygonDrawer;
import sample.tools.ScreenConvertor;
import sample.points.RealPoint;
import sample.points.ScreenPoint;
import java.util.ArrayList;

public class Rhombus implements IFigure{
    private ArrayList<ScreenPoint> points;
    private RealPoint center = new RealPoint(0, 0 );
    private final ArrayList<RealPoint> vertices = new ArrayList<>();
    private double diagonalX;
    private double diagonalY;

    public Rhombus(double xd, double yd){
        diagonalX = xd;
        diagonalY = yd;
    }
    public Rhombus(double xd, double yd, RealPoint center){
        diagonalX = xd;
        diagonalY = yd;
        setCenter(center);
    }

    public void setCenter(RealPoint center){
        this.center = center;
        updateVertices();
    }
    public void updateVertices(){
        vertices.clear();
        vertices.add(new RealPoint(center.getX() - diagonalX / 2, center.getY()));
        vertices.add(new RealPoint(center.getX(), center.getY() - diagonalY / 2));
        vertices.add(new RealPoint(center.getX() + diagonalX / 2, center.getY()));
        vertices.add(new RealPoint(center.getX(), center.getY() + diagonalY / 2));
    }

    @Override
    public void draw(ScreenConvertor screenConvertor, PixelWriter pixelWriter) {
        PolygonDrawer pd = new PolygonDrawer(pixelWriter, screenConvertor);
        pd.draw(vertices);
        setPoints(pd.getPoints());
    }

    public ArrayList<RealPoint> getVertices(){return vertices;}

    public void setDiagonalX(double diagonalX) {
        this.diagonalX = diagonalX;
        updateVertices();
    }
    public void setDiagonalY(double diagonalY) {
        this.diagonalY = diagonalY;
        updateVertices();
    }
    public ArrayList<ScreenPoint> getPoints(){return points;}
    public void setPoints(ArrayList<ScreenPoint> points){
        this.points = points;
    }
}
