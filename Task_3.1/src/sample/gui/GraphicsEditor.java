package sample.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.tools.ScreenConvertor;
import sample.affine.IAffine;
import sample.affine.Rotation;
import sample.affine.Scaling;
import sample.affine.Shift;
import sample.figures.IFigure;
import sample.figures.Line;
import sample.figures.Rhombus;
import sample.points.RealPoint;
import sample.points.ScreenPoint;
import java.util.ArrayList;

public class GraphicsEditor extends Application {
    private final Group group = new Group();
    private final Pane tools = new FlowPane();
    private int maxHeight;
    private int maxWidth;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final ScreenConvertor screenConvertor = new ScreenConvertor(
            -2, 2, 4, 4, 800, 600
    );
    private final Line xAxis = new Line(-1, 0, 1, 0);
    private final Line yAxis = new Line(0, -1, 0, 1);
    private Rhombus rhombus = new Rhombus(1, 1, new RealPoint(1, -0.2));
    private Line currentLine = null;
    private Line focus;
    private ScreenPoint prevDrag;

    public void createGraphicsEditor() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        maxWidth = 1300;
        maxHeight = 900;

        ArrayList<IFigure> arrayList = new ArrayList<>();
        arrayList.add(rhombus);
        //TextField rotate = new TextField("Поворот");
        Button enterRotate = new Button("Поворот");
        enterRotate.setOnAction(actionEvent -> {
            IAffine aRotate = new Rotation();
            double a = Math.PI / 4;
            double[][] matrix = {
                    {Math.cos(a), Math.sin(a)},
                    {-Math.sin(a), Math.cos(a)}
            };
            useAffine(aRotate, arrayList, matrix);
            render();
        });
        //TextField shift = new TextField("Сдвиг");
        Button enterShift = new Button("Сдвиг");
        enterShift.setOnAction(actionEvent -> {
            IAffine aShift = new Shift();
            double[][] matrix = {
                    {1, 1},
                    {0, 0}
            };
            useAffine(aShift, arrayList, matrix);
            render();
        });
        //TextField scaling = new TextField("Масштабирование");
        Button enterScaling = new Button("Масштабирование");
        enterScaling.setOnAction(actionEvent -> {
            IAffine aScaling = new Scaling();
            double[][] matrix = {
                    {-1, 0},
                    {0, 1}
            };
            useAffine(aScaling, arrayList, matrix);
            render();
        });
        tools.getChildren().addAll(enterRotate, enterShift, enterScaling);
        lines.add(xAxis);
        lines.add(yAxis);
        Scene scene = new Scene(group, maxWidth, maxHeight);
        scene.setOnMousePressed(this::mousePressed);
        scene.setOnMouseReleased(this::mouseReleased);
        scene.setOnMouseDragged(this::mouseDragged);
        scene.setOnMouseClicked(this::mouseClicked);
        scene.setOnScroll(this::mouseScroll);
        render();

        stage.setTitle("Graphics Editor");
        stage.setScene(scene);
        stage.show();
    }

    private void render() {
        WritableImage wi = new WritableImage(maxWidth, maxHeight);
        rhombus.draw(screenConvertor, wi.getPixelWriter());
        for (Line l :
                lines) {
            l.draw(screenConvertor, wi.getPixelWriter());
        }
        group.getChildren().clear();
        group.getChildren().add(new ImageView(wi));
        group.getChildren().add(tools);
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        ScreenPoint current = new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
        moveScreen(current);
        if (currentLine != null) {
            currentLine.setP2(screenConvertor.s2r(current));
        }
        render();
    }
    private void mousePressed(MouseEvent mouseEvent) {
        if(!onLine(mouseEvent)) {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                prevDrag = new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                currentLine = new Line(
                        screenConvertor.s2r(new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY())),
                        screenConvertor.s2r(new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY()))
                );
            }
            render();
        }
    }
    private void mouseReleased(MouseEvent mouseEvent) {
        if(!onLine(mouseEvent)) {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                prevDrag = null;
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                lines.add(currentLine);
                focusOn(currentLine);
                currentLine = null;
            }
            render();
        }
    }
    private void mouseClicked(MouseEvent mouseEvent) {
        for (Line l :
                lines) {
            for (ScreenPoint p :
                    l.getPoints()) {
                if (inCircle(p, 3, new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY()))) {
                    focus = l;
                    focusOn(focus);
                    System.out.println("Focus");
                    break;
                }
            }
        }
        if (focus != null) {
            Circle c1 = new Circle(screenConvertor.realToScreen(focus.getP1()).getX(),
                    screenConvertor.realToScreen(focus.getP1()).getY(),
                    10,
                    Color.BLUE);
            Circle c2 = new Circle(screenConvertor.realToScreen(focus.getP2()).getX(),
                    screenConvertor.realToScreen(focus.getP2()).getY(),
                    10,
                    Color.BLUE);
            group.getChildren().addAll(c1, c2);
        }
    }
    private void mouseScroll(ScrollEvent scrollEvent) {
        System.out.println(scrollEvent.getDeltaY());
        int clicks = (int) scrollEvent.getDeltaY() / 40;
        double scale = 1;
        double coef = clicks > 0 ? 0.9 : 1.1;
        for (int i = 0; i < Math.abs(clicks); i++) {
            scale *= coef;
        }
        screenConvertor.setW(screenConvertor.getW() * scale);
        screenConvertor.setH(screenConvertor.getH() * scale);
        render();
    }


    private void moveScreen(ScreenPoint current) {
        ScreenPoint delta;

        if (prevDrag != null) {
            delta = new ScreenPoint(
                    current.getX() - prevDrag.getX(),
                    current.getY() - prevDrag.getY());


            RealPoint deltaReal = screenConvertor.s2r(delta);
            RealPoint zeroReal = screenConvertor.s2r(new ScreenPoint(0, 0));
            RealPoint vector = new RealPoint(
                    deltaReal.getX() - zeroReal.getX(),
                    deltaReal.getY() - zeroReal.getY()
            );
            screenConvertor.setX(screenConvertor.getX() - vector.getX());
            screenConvertor.setY(screenConvertor.getY() - vector.getY());
            prevDrag = current;
        }
    }
    private void focusOn(Line line){
        focus = line;
        Circle c1 = new Circle(screenConvertor.realToScreen(focus.getP1()).getX(),
                screenConvertor.realToScreen(focus.getP1()).getY(),
                10,
                Color.BLUE);
        Circle c2 = new Circle(screenConvertor.realToScreen(focus.getP2()).getX(),
                screenConvertor.realToScreen(focus.getP2()).getY(),
                10,
                Color.BLUE);
        render();
        group.getChildren().addAll(c1, c2);
    }

    private boolean inCircle(ScreenPoint center, int radius, ScreenPoint point){
        return Math.sqrt(point.getX() - center.getX()) + Math.sqrt(point.getY() - center.getY()) < radius;
    }
    private boolean onLine(MouseEvent mouseEvent){
        for (Line l :
                lines) {
            for (ScreenPoint p :
                    l.getPoints()) {
                if (inCircle(p, 5, new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY()))) {
                    return true;
                }
            }
        }
        return false;
    }
    private void useAffine(IAffine iAffine, ArrayList<IFigure> figures, double[][] matrix){
        for (IFigure f :
                figures) {
            for (RealPoint sp :
                    f.getVertices()) {
                iAffine.convert(sp, matrix);
            }
        }
    }
}
