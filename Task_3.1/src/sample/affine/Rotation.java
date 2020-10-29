package sample.affine;

import sample.points.RealPoint;

public class Rotation implements IAffine {
    @Override
    public void convert(RealPoint point, double[][] matrix) {
        point.setX(point.getX() * matrix[0][0] + point.getY() * matrix[1][0]);
        point.setY(point.getX() * matrix[0][1] + point.getY() * matrix[1][1]);
    }
}
