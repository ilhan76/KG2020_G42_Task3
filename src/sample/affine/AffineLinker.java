package sample.affine;

import sample.points.RealPoint;
import java.util.ArrayList;

public class AffineLinker implements IAffine{
    double[][] matrix;
    private final ArrayList<IAffine> affineTransforms;
    public AffineLinker(ArrayList<IAffine> affineTransforms){
        this.affineTransforms = affineTransforms;
    }
    @Override
    public void convert(RealPoint point) {
        for (IAffine a :
                affineTransforms) {
            a.convert(point);
        }
    }

    @Override
    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }
    public void addAffine(IAffine affine){
        affineTransforms.add(affine);
    }
}
