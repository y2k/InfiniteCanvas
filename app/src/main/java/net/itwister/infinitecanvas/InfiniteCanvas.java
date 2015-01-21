package net.itwister.infinitecanvas;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created on 1/21/2015.
 */
class InfiniteCanvas extends Observable {

    private List<Curve> curves = new ArrayList<>();

    PointF offset = new PointF();

    public void updateViewPort(int left, int top, int right, int bottom) {
        // TODO
    }

    public void beginCurve() {
        curves.add(new Curve());
    }

    public void addPoint(float x, float y) {
        lastCurves().addPoint(x, y);
        notifyCurvesChanged();
    }

    public void endCurve() {
        lastCurves().optimize();
        notifyCurvesChanged();
    }

    private Curve lastCurves() {
        return curves.get(curves.size() - 1);
    }

    public float[] getVisibleScene() {
        return new FloatArraySerializer(curves).serialize();
    }

    public void offset(float x, float y) {
        offset.x = x;
        offset.y = y;
        notifyObservers();
    }

    private void notifyCurvesChanged() {
        setChanged();
        notifyObservers();
    }
}