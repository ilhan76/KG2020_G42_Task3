package sample.affine;

import sample.points.RealPoint;

public class Scaling implements IAffine{
    double[][] matrix;
    @Override
    public void convert(RealPoint point) {
        point.setX(point.getX() * matrix[0][0]);
        point.setY(point.getY() * matrix[1][1]);
    }

    @Override
    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }
}
