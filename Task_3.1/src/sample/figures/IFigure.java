package sample.figures;

import javafx.scene.image.PixelWriter;
import javafx.scene.shape.Circle;
import sample.tools.ScreenConvertor;
import sample.points.RealPoint;
import sample.points.ScreenPoint;

import java.util.ArrayList;

public interface IFigure {
    void draw(ScreenConvertor screenConvertor, PixelWriter pixelWriter);
    ArrayList<RealPoint> getVertices();
    ArrayList<ScreenPoint> getPoints();
    void setPoints(ArrayList<ScreenPoint> points);
    ArrayList<Circle> drawFocus(ScreenConvertor screenConvertor, PixelWriter pixelWriter);
}
