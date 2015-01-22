package net.itwister.infinitecanvas.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.itwister.infinitecanvas.canvas.InfiniteCanvas;

import java.util.Observable;
import java.util.Observer;

/**
 * Created on 1/21/2015.
 */
public class InfiniteCanvasView extends View implements Observer {

    InfiniteCanvas infiniteCanvas;
    private Paint defaultPaint;

    public InfiniteCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        defaultPaint = new Paint();
        defaultPaint.setColor(Color.DKGRAY);
        defaultPaint.setStrokeWidth(0);
        defaultPaint.setAntiAlias(true);

        infiniteCanvas = new InfiniteCanvas();
        infiniteCanvas.addObserver(this);
    }

    PointF startMovePoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() > 1) {
                    // TODO
                    startMovePoint = new PointF(event.getX(), event.getY());
                } else {
                    infiniteCanvas.beginCurve();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    // TODO
                    infiniteCanvas.offset(event.getX() - startMovePoint.x, event.getY() - startMovePoint.y);
                    startMovePoint = new PointF(event.getX(), event.getY());
                } else {
                    infiniteCanvas.addPoint(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                infiniteCanvas.endCurve();
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
    public void update(Observable observable, Object data) {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLines(infiniteCanvas.getVisibleScene(), defaultPaint);
    }
}