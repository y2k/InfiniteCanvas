package net.itwister.infinitecanvas.canvas;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 1/29/2015.
 */
class StringSerializer {

    String serialize(List<Curve> curves) {
        StringBuilder result = new StringBuilder();
        for (Curve c : curves)
            result.append(serialize(c)).append("|");
        return result.toString();
    }

    private StringBuilder serialize(Curve curve) {
        StringBuilder result = new StringBuilder();
        for (PointF p : curve.getPoints())
            result.append(p.x).append("x").append(p.y).append("x").append(curve.getColor()).append(";");
        return result;
    }

    List<Curve> deserialize(String data) {
        List<Curve> result = new ArrayList<>();
        for (String curvePart : data.split("\\|")) {
            Curve c = null;
            for (String pointPart : curvePart.split(";")) {
                String[] coordParts = pointPart.split("x");
                if (c == null) c = new Curve(Integer.parseInt(coordParts[2]));
                c.addPoint(Float.parseFloat(coordParts[0]), Float.parseFloat(coordParts[1]));
            }
            result.add(c);
        }
        return result;
    }
}