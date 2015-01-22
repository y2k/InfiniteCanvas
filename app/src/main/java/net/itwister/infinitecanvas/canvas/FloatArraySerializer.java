package net.itwister.infinitecanvas.canvas;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 1/21/2015.
 */
class FloatArraySerializer {

    private List<Curve> curves;
    private float[] buffer;

    FloatArraySerializer(List<Curve> curves) {
        this.curves = curves;
    }

    float[] serialize() {
        clearFromNotValidCurves();
        buffer = new float[computeBufferSize()];

        int pos = 0;
        for (Curve curve : curves) {
            PointF prevPoint = null;
            for (PointF point : curve.getPoints()) {
                if (prevPoint != null) {
                    addLineToBuffer(pos, prevPoint, point);
                    pos += 4;
                }
                prevPoint = point;
            }
        }
        return buffer;
    }

    private void clearFromNotValidCurves() {
        List<Curve> validCurves = new ArrayList<>();
        for (Curve i : curves)
            if (i.isValid())
                validCurves.add(i);
        curves = validCurves;
    }

    private void addLineToBuffer(int pos, PointF startPoint, PointF endPoint) {
        buffer[pos++] = startPoint.x;
        buffer[pos++] = startPoint.y;
        buffer[pos++] = endPoint.x;
        buffer[pos++] = endPoint.y;
    }

    private int computeBufferSize() {
        int totalLines = 0;
        for (Curve s : curves)
            totalLines += s.getPointCount() - 1;
        return totalLines * 4;
    }
}