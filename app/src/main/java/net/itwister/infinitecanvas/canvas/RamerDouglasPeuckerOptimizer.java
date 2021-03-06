package net.itwister.infinitecanvas.canvas;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 1/21/2015.
 */
class RamerDouglasPeuckerOptimizer {

    private float epsilon;
    private List<PointF> points;

    public void setEpsilon(float epsilon) {
        this.epsilon = epsilon;
    }

    public void setPoints(List<PointF> points) {
        this.points = points;
    }

    public List<PointF> compute() {
        return points.size() > 3
                ? douglasPeucker(points, 0, points.size() - 1, getAddaptiveEpsilon())
                : points;
    }

    private float getAddaptiveEpsilon() {
        float minx = points.get(0).x;
        float maxx = minx;
        float miny = points.get(0).y;
        float maxy = miny;
        for (PointF p : points) {
            minx = Math.min(minx, p.x);
            maxx = Math.max(maxx, p.x);
            miny = Math.min(miny, p.y);
            maxy = Math.max(maxy, p.y);
        }

        return epsilon * (maxx-minx + maxy - miny) / 2 / 100;
    }

    private static List<PointF> douglasPeucker(List<PointF> points, int startIndex, int lastIndex, float epsilon) {
        float dmax = 0f;
        int index = startIndex;

        for (int i = index + 1; i < lastIndex; ++i) {
            float d = pointLineDistance(points.get(i), points.get(startIndex), points.get(lastIndex));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        if (dmax > epsilon) {
            List<PointF> res1 = douglasPeucker(points, startIndex, index, epsilon);
            List<PointF> res2 = douglasPeucker(points, index, lastIndex, epsilon);

            List<PointF> finalRes = new ArrayList<>();
            for (int i = 0; i < res1.size() - 1; ++i) {
                finalRes.add(res1.get(i));
            }

            for (int i = 0; i < res2.size(); ++i) {
                finalRes.add(res2.get(i));
            }

            return finalRes;
        }
        else {
            return Arrays.asList(points.get(startIndex), points.get(lastIndex));
        }
    }

    private static float pointLineDistance(PointF point, PointF start, PointF end) {
        if (start == end) {
            return distance(point, start);
        }

        float n = Math.abs((end.x - start.x) * (start.y - point.y) - (start.x - point.x) * (end.y - start.y));
        double d = Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y));
        return (float) (n / d);
    }

    private static float distance(PointF value1, PointF value2)
    {
        return (float)Math.sqrt((value1.x - value2.x) * (value1.x - value2.x) + (value1.y - value2.y) * (value1.y - value2.y));
    }
}