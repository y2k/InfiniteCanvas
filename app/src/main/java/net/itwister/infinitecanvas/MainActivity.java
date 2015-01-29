package net.itwister.infinitecanvas;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.itwister.infinitecanvas.canvas.InfiniteCanvasView;
import net.itwister.infinitecanvas.common.FullScreenUtils;


public class MainActivity extends ActionBarActivity {

    InfiniteCanvasView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvas = (InfiniteCanvasView) findViewById(R.id.canvas);
        findViewById(R.id.undo).setOnClickListener(v -> canvas.getModel().undo());
        findViewById(R.id.redo).setOnClickListener(v -> canvas.getModel().redo());

        if (savedInstanceState != null)
            canvas.getModel().loadFromString(savedInstanceState.getString("canvas"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("canvas", canvas.getModel().saveToString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.createNew) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            overridePendingTransition(0, 0);
            return true;
        } else if (id == R.id.fullscreen) {
            FullScreenUtils.hideSystemUI(this);
        } else if (id==R.id.color) {
            new PaletteDialog(this, color -> canvas.getModel().setColor(color)).show();
        }

        return super.onOptionsItemSelected(item);
    }
}