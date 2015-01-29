package net.itwister.infinitecanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.example.hellocompute.ScriptC_palette;

import java.util.Observable;

/**
 * Created on 1/29/2015.
 */
public class PaletteView extends View {

    PaletteGenerator generator = new PaletteGenerator(getContext());

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

        private Bitmap lastCreateBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        private int size;
        private Context context;

        public PaletteGenerator(Context context) {
            this.context = context;
        }

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
                    RenderScript rs = RenderScript.create(context);
                    ScriptC_palette script = new ScriptC_palette(rs);

                    Allocation in = Allocation.createFromBitmap(rs, Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888));
                    Allocation out = Allocation.createTyped(rs, in.getType());
                    script.set_width(size);
                    script.set_height(size);
                    script.forEach_root(in, out);

                    Bitmap outBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                    out.copyTo(outBitmap);

                    return outBitmap;
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