package sample.figures;

import javafx.scene.image.PixelWriter;
import sample.Drawers.LineDrawer;
import sample.points.ScreenPoint;

import java.util.ArrayList;

public class IFigure {
    protected ArrayList<ScreenPoint> points;
    protected PixelWriter pw;
    protected LineDrawer ld;
}
