package net.itwister.infinitecanvas;

import android.graphics.Point;
import android.test.AndroidTestCase;

/**
 * Created on 1/21/2015.
 */
public class RamerDouglasPeuckerSimplifierTest extends AndroidTestCase {

    public void test() {
        Point[] testPoints = new Point[] {
                new Point(0, 0),
                new Point(50, 0),
                new Point(100, 0),
                new Point(100, 50),
                new Point(100, 100),
                new Point(50, 100),
                new Point(0, 100),
                new Point(0, 50),
                new Point(0, 0),
        };

        RamerDouglasPeuckerSimplifier simplifier = new RamerDouglasPeuckerSimplifier();
        simplifier.setEpsilon(0.1f);
        simplifier.setPoints(testPoints);
        Point[] actual = simplifier.compute();

        Point[] expected = new Point[] {
                new Point(0, 0),
                new Point(100, 0),
                new Point(100, 100),
                new Point(0, 100),
                new Point(0, 0),
        };

        assertEquals(expected, actual);
    }
}