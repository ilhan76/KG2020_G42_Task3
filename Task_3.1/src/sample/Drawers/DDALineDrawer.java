package sample.Drawers;



import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import sample.points.ScreenPoint;
import java.util.ArrayList;
import java.util.Collections;

public class DDALineDrawer implements LineDrawer {
    private final PixelWriter pw;
    private final ArrayList<ScreenPoint> countLinePoints = new ArrayList<>();

    public DDALineDrawer(PixelWriter pw) {
        this.pw = pw;
    }

    @Override
    public void drawLine(ScreenPoint p1, ScreenPoint p2) {
        countLinePoints.clear();
        int x2 = p2.getX();
        int x1 = p1.getX();
        int y2 = p2.getY();
        int y1 = p1.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        if (Math.abs(dx) > Math.abs(dy)) {
            if (x1 > x2) {
                int tmp = x1; x1 = x2; x2 = tmp;
                tmp = y1; y1 = y2; y2 = tmp;
            }
            double k = dy / dx;
            for (int j = x1; j <= x2; j++) {
                double i = k * (j - x1) + y1;
                pw.setColor(j, (int) i, javafx.scene.paint.Color.BLACK);
                countLinePoints.add(new ScreenPoint(j, (int) i));
            }
        } else {
            if (y1 > y2) {
                int tmp = x1; x1 = x2; x2 = tmp;
                tmp = y1; y1 = y2; y2 = tmp;
            }
            double kObr = dx / dy;
            for (int i = y1; i <= y2; i++) {
                double j = kObr * (i - y1) + x1;
                pw.setColor((int) j, i, Color.BLACK);
                countLinePoints.add(new ScreenPoint((int) j, i));
            }
        }
    }

    @Override
    public ArrayList<ScreenPoint> getPoints(){
       return countLinePoints;
    }
}
