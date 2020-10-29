package sample.affine;

import sample.points.RealPoint;

public class Scaling implements IAffine{
    @Override
    public void convert(RealPoint point, double[][] matrix) {
        point.setX(point.getX() * matrix[0][0]);
        point.setY(point.getY() * matrix[1][1]);
        System.out.println(point.getX());
        System.out.println(point.getY());
    }
}
