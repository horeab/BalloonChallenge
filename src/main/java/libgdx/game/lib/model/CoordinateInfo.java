package libgdx.game.lib.model;

import org.apache.commons.lang3.tuple.Pair;

import libgdx.game.lib.util.MatrixValue;

public class CoordinateInfo {

    private Pair point;
    private MatrixValue matrixValue;

    public Pair<Integer, Integer> getPoint() {
        return point;
    }

    public void setPoint(Pair point) {
        this.point = point;
    }

    public MatrixValue getMatrixValue() {
        return matrixValue;
    }

    public void setMatrixValue(MatrixValue matrixValue) {
        this.matrixValue = matrixValue;
    }
}
