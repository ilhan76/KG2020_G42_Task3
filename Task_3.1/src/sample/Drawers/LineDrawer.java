package sample.Drawers;


import sample.points.ScreenPoint;

import java.util.ArrayList;

public interface LineDrawer {
    void drawLine(ScreenPoint p1, ScreenPoint p2);
    ArrayList<ScreenPoint> getPoints();
}
