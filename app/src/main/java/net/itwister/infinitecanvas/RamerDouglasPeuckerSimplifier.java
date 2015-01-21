package net.itwister.infinitecanvas;

import android.graphics.Point;

/**
 * Created on 1/21/2015.
 */
public class RamerDouglasPeuckerSimplifier {

    private float epsilon;
    private Point[] points;

    public void setEpsilon(float epsilon) {
        this.epsilon = epsilon;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }

    public Point[] compute() {
        return new Point[0];
    }
}