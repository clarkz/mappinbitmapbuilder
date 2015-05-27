package com.rdcmappinrenderingresearch.clzhang.mappinrenderingtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends Activity {
    final private String LOG_MESSAGE = "Total time (in mls) ";
    final private int BITMAP_COUNT = 1000;

    private MapPinBitmapBuilder mPinBuilder = null;
    private NinePatchDrawable mBackground9Patch = null;
    private NinePatchDrawable mBackgroundTop9Patch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPinBuilder = MapPinBitmapBuilder.getBuilder(getBaseContext());
        mPinBuilder.setTextColor(Color.WHITE).setTextSizeInSp(12);

        mBackground9Patch = (NinePatchDrawable) getResources()
                .getDrawable(R.drawable.mappin_basic, null);
        mBackgroundTop9Patch = (NinePatchDrawable) getResources()
                .getDrawable(R.drawable.mappin_basic_top, null);
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
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTemplateRun(View v) {
        TextView pinView = (TextView) findViewById(R.id.basic_map_pin);
        pinView.setDrawingCacheEnabled(true);

        long start = new Date().getTime();

        for (int i = 0; i < BITMAP_COUNT; i++) {
            getBitmapFromTemplate(pinView, i + "");
        }
        Log.d("onTemplateRun", LOG_MESSAGE + "for " + BITMAP_COUNT + " records:" + (new Date().getTime() - start));

    }

    private Bitmap getBitmapFromTemplate(TextView template, String text) {
        template.setText(text);
        template.requestLayout();

        Bitmap bitmap = template.getDrawingCache(true);
        return bitmap.copy(bitmap.getConfig(), true);
    }

    public void onEngineRun(View v) {
        long start = new Date().getTime();
        for (int i = 0; i < BITMAP_COUNT; i++) {
            getBitmapFromBuilder(Integer.toString(i));
        }
        Log.d("onEngineRun", LOG_MESSAGE + "for " + BITMAP_COUNT + " records:" + (new Date().getTime() - start));
    }

    private Bitmap getBitmapFromBuilder(String text) {
        return mPinBuilder
                .setBackgroundDrawable(mBackground9Patch)
                .setBackgroundTopDrawable(mBackgroundTop9Patch)
                .setBackgroundColor(Color.RED)
                .setLeftIcon(null)
                .setRightIcon(null)
                .setText(text)
                .getIcon();

    }
}
