package net.itwister.infinitecanvas;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.android.example.hellocompute.ScriptC_palette;

import java.util.Observable;

/**
 * Created on 1/29/2015.
 */
public class PaletteDialog extends Dialog {

    public PaletteDialog(Context context, OnColorSelectListener listener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new PaletteView(context, color -> {
            dismiss();
            listener.OnColorSelected(color);
        }));
    }

    static interface OnColorSelectListener {

        void OnColorSelected(int color);
    }

    @SuppressLint("ViewConstructor")
    static class PaletteView extends View {

        private OnColorSelectListener listener;
        private PaletteGenerator generator = new PaletteGenerator(getContext());

        public PaletteView(Context context, OnColorSelectListener listener) {
            super(context);
            this.listener = listener;
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
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            int color = generator.getColorForPoint(event.getX(), event.getY());
            listener.OnColorSelected(color);
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

                recreatePalette();
                setChanged();
                notifyObservers();
            }

            private void recreatePalette() {
                RenderScript rs = RenderScript.create(context);
                ScriptC_palette script = new ScriptC_palette(rs);

                Allocation in = Allocation.createFromBitmap(rs, Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888));
                Allocation out = Allocation.createTyped(rs, in.getType());
                script.set_width(size);
                script.set_height(size);
                script.forEach_root(in, out);

                Bitmap outBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                out.copyTo(outBitmap);

                lastCreateBitmap = outBitmap;
            }

            public int getColorForPoint(float x, float y) {
                return lastCreateBitmap.getPixel((int)x, (int)y);
            }
        }
    }
}