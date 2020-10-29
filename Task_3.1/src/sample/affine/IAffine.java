package sample.affine;

import sample.points.RealPoint;

public interface IAffine {
    void convert(RealPoint point, double[][] matrix);
}
