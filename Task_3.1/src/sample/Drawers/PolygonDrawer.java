package sample.Drawers;

import javafx.scene.image.PixelWriter;

import java.awt.*;
import java.util.List;

public class PolygonDrawer {
    private final PixelWriter pw;
    private final LineDrawer ld;

    public PolygonDrawer(PixelWriter pw, LineDrawer ld, List<Point> points) {
        this.pw = pw;
        this.ld = ld;
    }
    public void draw(){

    }
}
