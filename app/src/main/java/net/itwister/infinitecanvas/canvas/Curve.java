package net.itwister.infinitecanvas.canvas;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created on 1/21/2015.
 */
class Curve {

    private List<PointF> points = new ArrayList<>();
    private int color;

    public Curve(int color) {
        this.color = color;
    }

    public void addPoint(float x, float y) {
        points.add(new PointF(x, y));
    }

    public void optimize() {
        RamerDouglasPeuckerOptimizer optimizer = new RamerDouglasPeuckerOptimizer();
        optimizer.setPoints(points);
        optimizer.setEpsilon(1);
        points = optimizer.compute();
    }

    public int getPointCount() {
        return points.size();
    }

    public Iterable<PointF> getPoints() {
        return Collections.unmodifiableCollection(points);
    }

    public boolean isValid() {
        return points.size() >= 2;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}