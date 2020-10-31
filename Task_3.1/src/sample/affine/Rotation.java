package sample.affine;

import sample.points.RealPoint;

public class Rotation implements IAffine {
    double[][] matrix;
    @Override
    public void convert(RealPoint point) {
        double dx = point.getX() * matrix[0][0] - point.getY() * matrix[1][0];
        double dy = point.getX() * matrix[0][1] + point.getY() * matrix[1][1];
        point.setX(dx);
        point.setY(dy);
    }

    @Override
    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }
}
