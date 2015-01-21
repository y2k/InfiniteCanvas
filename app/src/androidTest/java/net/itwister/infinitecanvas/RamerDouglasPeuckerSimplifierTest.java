package net.itwister.infinitecanvas;

import android.graphics.PointF;
import android.test.AndroidTestCase;

/**
 * Created on 1/21/2015.
 */
public class RamerDouglasPeuckerSimplifierTest extends AndroidTestCase {

    static PointF[] TEST_POINTS = new PointF[] {
            new PointF(0, 0),
            new PointF(50, 0),
            new PointF(100, 0),
            new PointF(100, 50),
            new PointF(100, 100),
            new PointF(50, 100),
            new PointF(0, 100),
            new PointF(1, 1),
    };

    private RamerDouglasPeuckerSimplifier simplifier;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        simplifier = new RamerDouglasPeuckerSimplifier();
        simplifier.setPoints(TEST_POINTS);
    }

    public void testEpsilonZero() {
        simplifier.setEpsilon(1.0f);
        PointF[] actual = simplifier.compute();
        PointF[] expected = new PointF[] {
                new PointF(0, 0),
                new PointF(100, 0),
                new PointF(100, 100),
                new PointF(0, 100),
                new PointF(1, 1),
        };
        assertCollections(expected, actual);
    }

    private void assertCollections(PointF[] expected, PointF[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++)
            assertEquals(expected[i], actual[i]);
    }
}