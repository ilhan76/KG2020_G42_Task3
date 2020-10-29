package sample.affine;

import sample.points.RealPoint;

public class Shift implements IAffine {
    @Override
    public void convert(RealPoint point, double[][]matrix) {
        point.setX(point.getX() + matrix[0][0]);
        point.setY(point.getY() + matrix[0][1]);
    }
}
