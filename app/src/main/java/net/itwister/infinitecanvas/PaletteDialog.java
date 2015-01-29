package net.itwister.infinitecanvas;

import android.app.Dialog;
import android.content.Context;

/**
 * Created on 1/29/2015.
 */
public class PaletteDialog extends Dialog {

    public PaletteDialog(Context context) {
        super(context);
        setContentView(new PaletteView(context));
    }
}