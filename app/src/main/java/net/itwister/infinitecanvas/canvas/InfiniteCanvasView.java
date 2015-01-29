package net.itwister.infinitecanvas.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created on 1/21/2015.
 */
public class InfiniteCanvasView extends View {

    private InfiniteCanvas infiniteCanvas = new InfiniteCanvas();
    private Paint drawPaint = new Paint();
    private PointF startMovePoint;

    public InfiniteCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawPaint.setStrokeWidth(0);
        drawPaint.setAntiAlias(true);

        infiniteCanvas.addObserver((observable, data) -> invalidate());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startMovePoint = new PointF(event.getX(), event.getY());
                infiniteCanvas.beginCurve();
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    infiniteCanvas.addOffset(event.getX() - startMovePoint.x, event.getY() - startMovePoint.y);
                    startMovePoint = new PointF(event.getX(), event.getY());
                } else {
                    infiniteCanvas.addPoint(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                infiniteCanvas.endCurve();
                startMovePoint = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(0, 0, 0, 0); // XXX:
        canvas.translate(infiniteCanvas.getOffset().x, infiniteCanvas.getOffset().y);

        for (Curve c : infiniteCanvas.getVisibleCurves()) {
            drawPaint.setColor(c.getColor());
            canvas.drawLines(new FloatArraySerializer(c).serialize(), drawPaint);
        }
    }

    public InfiniteCanvas getModel() {
        return infiniteCanvas;
    }
}