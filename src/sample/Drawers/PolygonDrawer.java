package sample.Drawers;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sample.tools.ScreenConvertor;
import sample.points.RealPoint;
import sample.points.ScreenPoint;
import java.util.ArrayList;
import java.util.List;

public class PolygonDrawer {
    private final ArrayList<ScreenPoint> countPolPoints = new ArrayList<>();
    private final LineDrawer ld;
    private final ScreenConvertor screenConvertor;

    public PolygonDrawer(PixelWriter pw, ScreenConvertor screenConvertor) {
        this.ld = new DDALineDrawer(pw);
        this.screenConvertor = screenConvertor;
    }

    public void draw(List<RealPoint> vertices){
        countPolPoints.clear();
        RealPoint count = vertices.get(0);
        for (int i = 1; i < vertices.size(); i++) {
            ld.drawLine(screenConvertor.realToScreen(count), screenConvertor.realToScreen(vertices.get(i)));
            count = vertices.get(i);
            countPolPoints.addAll(ld.getPoints());
        }
        ld.drawLine(screenConvertor.realToScreen(vertices.get(0)), screenConvertor.realToScreen(vertices.get(vertices.size() - 1)));
        countPolPoints.addAll(ld.getPoints());
    }

    public ArrayList<ScreenPoint> getPoints(){return countPolPoints;}
}
