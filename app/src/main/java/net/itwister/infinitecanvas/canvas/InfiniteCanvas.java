package net.itwister.infinitecanvas.canvas;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created on 1/21/2015.
 */
public class InfiniteCanvas extends Observable {

    private List<Curve> curves = new ArrayList<>();
    private UndoManager undoManager = new UndoManager(curves, this);

    PointF offset = new PointF();

    public void updateViewPort(int left, int top, int right, int bottom) {
        // TODO
    }

    public void beginCurve() {
        curves.add(new Curve());
    }

    public void addPoint(float x, float y) {
        lastCurves().addPoint(x - offset.x, y - offset.y);
        notifyObservers();
    }

    public void endCurve() {
        if (lastCurves().isValid()) {
            lastCurves().optimize();
            undoManager.clear();
        } else curves.remove(lastCurves());
        notifyObservers();
    }

    private Curve lastCurves() {
        return curves.get(curves.size() - 1);
    }

    public float[] getVisibleScene() {
        return new FloatArraySerializer(curves).serialize();
    }

    public void addOffset(float x, float y) {
        offset.x += x;
        offset.y += y;
        notifyObservers();
    }

    public PointF getOffset() {
        return offset;
    }

    public void dump() {
        Log.v("InfiniteCanvas", "offset.x = " + offset.x + ", offset.y = " + offset.y);
    }

    public void undo() {
        undoManager.undo();
    }

    public void redo() {
        undoManager.redo();
    }

    @Override
    public boolean hasChanged() {
        return true;
    }
}