package sample.figures;

import javafx.scene.image.PixelWriter;
import sample.Drawers.LineDrawer;
import sample.points.ScreenPoint;

public class Rhombus extends IFigure{
    private ScreenPoint center = new ScreenPoint(0, 0 );
    private int DiagonalX;
    private int DiagonalY;
    Rhombus(int xd, int yd, PixelWriter pw, LineDrawer ld){
        DiagonalX = xd;
        DiagonalY = yd;
        this.pw = pw;
        this.ld = ld;
    }

    public void drawRhombus(){
        //PolygonDrawer pd = new PolygonDrawer()
    }
}
