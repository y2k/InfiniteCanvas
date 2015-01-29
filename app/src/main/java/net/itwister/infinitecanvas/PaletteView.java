package net.itwister.infinitecanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Observable;

/**
 * Created on 1/29/2015.
 */
public class PaletteView extends View {

    PaletteGenerator generator = new PaletteGenerator();

    public PaletteView(Context context) {
        super(context);
        generator.addObserver((observable, data) -> invalidate());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(generator.getBitmap(), 0, 0, null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        generator.invalidateSize(right - left);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultViewSize(), getDefaultViewSize());
    }

    private int getDefaultViewSize() {
        return (int) (300 * getResources().getDisplayMetrics().density);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int color = generator.getColorForPoint(event.getX(), event.getY());
        return super.onTouchEvent(event);
    }

    private static class PaletteGenerator extends Observable {

        Bitmap lastCreateBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        int size;

        public Bitmap getBitmap() {
            return lastCreateBitmap;
        }

        public void invalidateSize(int size) {
            if (this.size == size) return;
            this.size = size;
            Log.v("PaletteGenerator", "invalidateSize(size = " + size + ")");

            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    int[] buf = new int[size * size];
                    int pos = 0;
                    float[] hsv = new float[] { 0, 1, 0 };
                    for (int y = 0; y < size; y+=1) {
                        hsv[2] = 1 - (float) y / size;
                        for (int x = 0; x < size; x += 1) {
                            hsv[0] = 360f * x / size;
                            buf[pos++] = Color.HSVToColor(0xFF, hsv);
                        }
                    }

                    Bitmap result = Bitmap.createBitmap(buf, size, size, Bitmap.Config.ARGB_8888);
                    return result;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    lastCreateBitmap = result;
                    setChanged();
                    notifyObservers();
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        public int getColorForPoint(float x, float y) {
            return 0;
        }
    }
}