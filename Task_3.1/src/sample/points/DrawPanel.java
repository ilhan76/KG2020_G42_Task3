package sample.points;

import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sample.Drawers.DDALineDrawer;
import sample.Drawers.LineDrawer;
import sample.figures.Line;
import sample.ScreenConvertor;

import java.util.ArrayList;

public class DrawPanel extends Pane {
    private int maxHeight;
    private int maxWidth;
    private ArrayList<Line> lines = new ArrayList<>();
    private ScreenConvertor sc = new ScreenConvertor(
            -2, 2, 4, 4, 800, 600
    );
    private WritableImage wi;
    private Line xAxis = new Line(-1, 0, 1, 0);
    private Line yAxis = new Line(0, -1, 0, 1);
    private Line currentLine = null;
    private ScreenPoint prevDrag;

    public DrawPanel(int w, int h){
        maxHeight = h;
        maxWidth = w;
        wi = new WritableImage(w, h);
        repaint();
    }

    public void MouseDragged(MouseEvent mouseEvent){
        ScreenPoint current = new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
        moveScreen(current);
        if(currentLine != null){
            currentLine.setP2(sc.s2r(current));
        }
        System.out.println("Dragged");
        repaint();
    }
    public void MousePressed(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            prevDrag = new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
            System.out.println("2");
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            currentLine = new Line(
                    sc.s2r(new ScreenPoint((int) mouseEvent.getX(), (int)mouseEvent.getY())),
                    sc.s2r(new ScreenPoint((int)mouseEvent.getX(), (int)mouseEvent.getY()))
            );
            System.out.println("1");
        }
        repaint();
    }
    public void MouseReleased(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            prevDrag = null;
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            lines.add(currentLine);
            currentLine = null;
        }
        System.out.println("Released");
        //repaint();
        clear();
    }

    private void moveScreen(ScreenPoint current) {
        ScreenPoint delta;

        if (prevDrag != null) {
            delta = new ScreenPoint(
                    current.getX() - prevDrag.getX(),
                    current.getY() - prevDrag.getY());


            RealPoint deltaReal = sc.s2r(delta);
            RealPoint zeroReal = sc.s2r(new ScreenPoint(0, 0));
            RealPoint vector = new RealPoint(
                    deltaReal.getX() - zeroReal.getX(),
                    deltaReal.getY() - zeroReal.getY()
            );
            sc.setX(sc.getX() - vector.getX());
            sc.setY(sc.getY() - vector.getY());
            prevDrag = current;
        }
    }

    private void repaint(){
        wi = new WritableImage(maxWidth, maxHeight);
        LineDrawer ld = new DDALineDrawer(wi.getPixelWriter());
        ld.drawLine(sc.realToScreen(xAxis.getP1()), sc.realToScreen(xAxis.getP2()));
        ld.drawLine(sc.realToScreen(yAxis.getP1()), sc.realToScreen(yAxis.getP2()));
        for (Line l :
                lines) {
            ld.drawLine(sc.realToScreen(l.getP1()), sc.realToScreen(l.getP2()));
        }
    }
    public WritableImage getWi(){
        return wi;
    }
    public void clear(){
        wi = new WritableImage(maxWidth, maxHeight);
    }

}
