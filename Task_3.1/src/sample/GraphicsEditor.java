package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.Drawers.DDALineDrawer;
import sample.Drawers.LineDrawer;
import sample.figures.Line;
import sample.points.RealPoint;
import sample.points.ScreenPoint;

import java.util.ArrayList;

public class GraphicsEditor extends Application {
    private Pane pane = new Pane();
    private int maxHeight;
    private int maxWidth;
    private ArrayList<Line> lines = new ArrayList<>();
    private ScreenConvertor sc = new ScreenConvertor(
            -2, 2, 4, 4, 800, 600
    );
    private WritableImage wi;
    private ImageView imageView;
    private Line xAxis = new Line(-1, 0, 1, 0);
    private Line yAxis = new Line(0, -1, 0, 1);
    private Line currentLine = null;
    private ScreenPoint prevDrag;

    public void createGraphicsEditor(int w, int h){
        maxHeight = h;
        maxWidth = w;
        wi = new WritableImage(w, h);
        imageView = new ImageView(wi);
        render();
        Application.launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        //pane.getChildren().add(new Circle(50,50, 50, Color.GREEN));
        Scene scene = new Scene(pane, maxWidth, maxHeight);

        pane.setOnMouseClicked(mouseEvent -> {
            System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
        });
        pane.getChildren().add(imageView);
        stage.setTitle("Graphics Editor");
        stage.setScene(scene);
        stage.show();
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

    private void render(){
        wi = new WritableImage(maxWidth, maxHeight);
        LineDrawer ld = new DDALineDrawer(wi.getPixelWriter());
        ld.drawLine(sc.realToScreen(xAxis.getP1()), sc.realToScreen(xAxis.getP2()));
        ld.drawLine(sc.realToScreen(yAxis.getP1()), sc.realToScreen(yAxis.getP2()));
        for (Line l :
                lines) {
            ld.drawLine(sc.realToScreen(l.getP1()), sc.realToScreen(l.getP2()));
        }
        imageView = new ImageView(wi);
        //pane.getChildren().add(new ImageView(wi));
        //return new ImageView(wi);
    }
}
