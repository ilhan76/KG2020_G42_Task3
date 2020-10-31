package sample.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.affine.*;
import sample.tools.ScreenConvertor;
import sample.figures.IFigure;
import sample.figures.Line;
import sample.figures.Rhombus;
import sample.points.RealPoint;
import sample.points.ScreenPoint;
import java.util.ArrayList;

public class GraphicsEditor extends Application {
    private final Group group = new Group();
    private final Pane toolsAffine = new HBox();
    private final Pane tools = new VBox();
    private int maxHeight;
    private int maxWidth;
    private final ArrayList<IFigure> figures = new ArrayList<>();
    private final ScreenConvertor screenConvertor = new ScreenConvertor(
            -2, 2, 4, 4, 800, 800
    );
    private final Line xAxis = new Line(-1, 0, 1, 0);
    private final Line yAxis = new Line(0, -1, 0, 1);
    private IFigure focusFigure;
    private Line currentLine = null;
    //private boolean sf;
    //private boolean moveFlag = false;
    private ScreenPoint prevDrag;

    public void createGraphicsEditor() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        maxWidth = 1300;
        maxHeight = 900;

        IFigure r0 = new Rhombus(1, 2, new RealPoint(1, -0.2));
        figures.add(r0);
        focusFigure = figures.get(0);
        IFigure r1 = new Rhombus(1, 1, new RealPoint(-1, -0.5));
        figures.add(r1);
        IFigure r2 = new Rhombus(1, 1, new RealPoint(1, 0.5));
        figures.add(r2);
        IFigure r3 = new Rhombus(1, 1.5, new RealPoint(-1.5, -1));
        figures.add(r3);
        IFigure r4 = new Rhombus(0.4, 1, new RealPoint(0, 0.2));
        figures.add(r4);
        focusFigure = r0;


        TextField rotate = new TextField("1.5");
        Button enterRotate = new Button("Поворот");
        IAffine aRotate = new Rotation();
        enterRotate.setOnAction(actionEvent -> {
            double[][] matrix = toMatrix(rotate.getText(), AffineEnum.ROTATE);
            aRotate.setMatrix(matrix);
            useAffineToFigure(aRotate, focusFigure);
            render();
        });

        TextField shift = new TextField("1 1 0 0");
        Button enterShift = new Button("Сдвиг");
        IAffine aShift = new Shift();
        enterShift.setOnAction(actionEvent -> {
            double[][] matrix = toMatrix(shift.getText(), AffineEnum.SHIFT);
            aShift.setMatrix(matrix);
            useAffineToFigure(aShift, focusFigure);
            render();
        });

        TextField scaling = new TextField("-1 0 0 1");
        Button enterScaling = new Button("Масштабирование");
        IAffine aScaling = new Scaling();
        enterScaling.setOnAction(actionEvent -> {
            double[][] matrix = toMatrix(scaling.getText(), AffineEnum.SCALING);
            aScaling.setMatrix(matrix);
            useAffineToFigure(aScaling, focusFigure);
            render();
        });

        Button enterAllAffine = new Button("Все преобразования");
        enterAllAffine.setOnAction(actionEvent -> {
            ArrayList<IAffine> list = new ArrayList<>();
            double[][] matrix = toMatrix(rotate.getText(), AffineEnum.ROTATE);
            aRotate.setMatrix(matrix);
            matrix = toMatrix(shift.getText(), AffineEnum.SHIFT);
            aShift.setMatrix(matrix);
            matrix = toMatrix(scaling.getText(), AffineEnum.SCALING);
            aScaling.setMatrix(matrix);
            list.add(aRotate);
            list.add(aShift);
            list.add(aScaling);
            IAffine affine = new AffineLinker(list);
            useAffineToFigure(affine, focusFigure);
            render();
        });

        TextField newRhombus = new TextField();
        Button enterNewRhombus = new Button("Создать ромб");
        enterNewRhombus.setMinWidth(150);
        enterNewRhombus.setOnAction(actionEvent -> {
            String[] strings = newRhombus.getText().split(" ");
            double[] param = new double[4];
            for (int i = 0; i < 4; i++) param[i] = Double.parseDouble(strings[i]);
            Rhombus rhombus = new Rhombus(param[0], param[1], new RealPoint(param[2], param[3]));
            figures.add(rhombus);
            focusFigure = rhombus;
            render();
        });

        Button enterChangeFocusFigure = new Button("Изменить фокус");
        enterChangeFocusFigure.setMinWidth(150);
        enterChangeFocusFigure.setOnAction(actionEvent -> {
            changeFocus();
        });

        Button enterDelete = new Button("Удалить");
        enterDelete.setMinWidth(150);
        enterDelete.setOnAction(actionEvent -> {
            figures.remove(focusFigure);
            changeFocus();
            render();
        });

        toolsAffine.getChildren().addAll(rotate, enterRotate, shift, enterShift, scaling, enterScaling, enterAllAffine);
        tools.getChildren().addAll(enterChangeFocusFigure, enterDelete, newRhombus, enterNewRhombus);
        figures.add(xAxis);
        figures.add(yAxis);
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
    private void changeFocus(){
        if(focusFigure != null && figures.size() != 0){
            int index = figures.indexOf(focusFigure);
            if(index != figures.size() - 1) focusFigure = figures.get(++index);
            else focusFigure = figures.get(0);
            render();
        }
    }
    private double[][] toMatrix(String string, AffineEnum affineEnum){
        String[] subStr;
        subStr = string.split(" ");
        if(affineEnum == AffineEnum.ROTATE){
            double a = Double.parseDouble(string);
            return new double[][]{
                    {Math.cos(a), Math.sin(a)},
                    {Math.sin(a), Math.cos(a)}
            };
        } else if (affineEnum == AffineEnum.SCALING){
            double[][] matrix = new double[2][2];
            for (int i = 0; i < 2; i++) matrix[0][i] = Double.parseDouble(subStr[i]);
            for (int i = 0; i < 2; i++) matrix[1][i] = Double.parseDouble(subStr[i + 2]);
            return matrix;
        } else {
            double[][] matrix = new double[1][2];
            for (int i = 0; i < 2; i++) matrix[0][i] = Double.parseDouble(subStr[i]);
            return matrix;
        }
    }

    private void render() {
        WritableImage wi = new WritableImage(maxWidth * 2, maxHeight * 2);
        ArrayList<Circle> circles = focusFigure.drawFocus(screenConvertor, wi.getPixelWriter());
        for (IFigure f :
                figures) {
            f.draw(screenConvertor, wi.getPixelWriter());
        }
        group.getChildren().clear();
        for (Circle c :
                circles) {
            group.getChildren().add(c);
        }
        tools.setLayoutY(25);
        group.getChildren().addAll(new ImageView(wi), toolsAffine, tools);
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        ScreenPoint current = new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
        moveScreen(current);
        if (currentLine != null) currentLine.setP2(screenConvertor.s2r(current));
        render();
    }
    private void mousePressed(MouseEvent mouseEvent) {
        /*
        if(focus != null){
            if(inCircle(screenConvertor.realToScreen(focus.getP1()), 10, new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY()))){
                moveFlag = true;
                sf = true;
                System.out.println("Start");
                System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
                return;
            }
            else if (inCircle(screenConvertor.realToScreen(focus.getP2()), 10, new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY()))){
                moveFlag = true;
                sf = false;
                System.out.println("Finish");
                System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
                return;
            }
        }
         */
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
        /*
        if(moveFlag){
            if(sf) focus.setP1(screenConvertor.s2r(new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY())));
            else focus.setP2(screenConvertor.s2r(new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY())));
            moveFlag = false;
            render();
            System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
            return;
        }
         */
        if(!onLine(mouseEvent)) {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                prevDrag = null;
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                figures.add(currentLine);
                focusFigure = currentLine;
                currentLine = null;
            }
            render();
        }
    }
    private void mouseClicked(MouseEvent mouseEvent) {
        for (IFigure l :
                figures) {
            for (ScreenPoint p :
                    l.getPoints()) {
                if (inCircle(p, 3, new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY()))) {
                    focusFigure = l;
                    System.out.println("Focus");
                    render();
                    break;
                }
            }
        }
    }
    private void mouseScroll(ScrollEvent scrollEvent) {
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

    private boolean inCircle(ScreenPoint center, int radius, ScreenPoint point){
        return Math.sqrt(point.getX() - center.getX()) + Math.sqrt(point.getY() - center.getY()) <= Math.sqrt(radius);
    }
    private boolean onLine(MouseEvent mouseEvent){
        for (IFigure l :
                figures) {
            for (ScreenPoint p :
                    l.getPoints()) {
                if (inCircle(p, 5, new ScreenPoint((int) mouseEvent.getX(), (int) mouseEvent.getY()))) {
                    return true;
                }
            }
        }
        return false;
    }
    private void useAffine(IAffine iAffine, ArrayList<IFigure> figures){
        for (IFigure f :
                figures) {
            for (RealPoint rp :
                    f.getVertices()) {
                iAffine.convert(rp);
            }
        }
    }
    private void useAffineToFigure(IAffine iAffine, IFigure figure){
        for (RealPoint rp :
                figure.getVertices()) {
            System.out.println(rp.getX() + " " + rp.getY());
            iAffine.convert(rp);
            System.out.println(rp.getX() + " " + rp.getY());
        }
    }
}
