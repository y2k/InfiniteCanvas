package net.itwister.infinitecanvas.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

/**
 * Created on 1/21/2015.
 */
public class InfiniteCanvasView extends View {

    private InfiniteCanvas infiniteCanvas = new InfiniteCanvas();
    private Paint defaultPaint = new Paint();

    public InfiniteCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        defaultPaint.setColor(Color.DKGRAY);
        defaultPaint.setStrokeWidth(0);
        defaultPaint.setAntiAlias(true);

        infiniteCanvas.addObserver((observable, data) -> invalidate());
    }

    PointF startMovePoint;

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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        infiniteCanvas.updateViewPort(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(0, 0, 0, 0); // XXX: избавиться если понять как сбрасывать кэш
        canvas.translate(infiniteCanvas.getOffset().x, infiniteCanvas.getOffset().y);
        canvas.drawLines(infiniteCanvas.getVisibleScene(), defaultPaint);
    }

    public InfiniteCanvas getModel() {
        return infiniteCanvas;
    }
}