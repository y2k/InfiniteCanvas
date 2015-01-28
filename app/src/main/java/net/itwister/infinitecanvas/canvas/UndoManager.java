package net.itwister.infinitecanvas.canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created on 1/28/2015.
 */
class UndoManager {

    private List<Curve> trash = new ArrayList<>();
    private List<Curve> curves;
    private Observable observable;

    UndoManager(List<Curve> curves, Observable observable) {
        this.curves = curves;
        this.observable = observable;
    }

    public void undo() {
        if (!curves.isEmpty()) {
            trash.add(curves.remove(curves.size() - 1));
            notifyCurvesChanged();
        }
    }

    public void redo() {
        if (!trash.isEmpty()) {
            curves.add(trash.remove(trash.size() - 1));
            notifyCurvesChanged();
        }
    }

    private void notifyCurvesChanged() {
        observable.notifyObservers();
    }

    public void clear() {
        trash.clear();
    }
}