package com.rdcmappinrenderingresearch.clzhang.mappinrenderingtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends Activity {
    final private String LOG_MESSAGE = "Total time (in mls) ";
    final private int BITMAP_COUNT = 1000;

    private InternMapPinBuilder mPinBuilder = null;
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

        mPinBuilder = InternMapPinBuilder.getBuilder(getBaseContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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


    public void onEngineRunOnBackground(View v){
        long start = new Date().getTime();
        AsyncTask<Void, Void, Bitmap[]> task = new AsyncTask<Void, Void, Bitmap[]>() {
            @Override
            protected Bitmap[] doInBackground(Void... params) {
                return mPinBuilder.getBitmapArray(BITMAP_COUNT);
            }

            @Override
            protected void onPostExecute(Bitmap[] bitmaps) {
                showAllBitmaps(bitmaps);
            }
        };
        task.execute();
        Log.d("onEngineRunOnBackground", LOG_MESSAGE + "for " + BITMAP_COUNT + " records:" + (new Date().getTime() - start));
    }

    public void onEngineRunOnBackgroundOneByOne(View v){
        long start = new Date().getTime();
        for(int i = 0; i < BITMAP_COUNT; i++) {
            AsyncTask<Integer, Void, Bitmap> task = new AsyncTask<Integer, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Integer... params) {
                    return mPinBuilder.getForsaleBitmapIcon("sicon" + params[0]);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    showBitmap(bitmap);
                }
            };
            task.execute(i);
        }
        Log.d("RunOnBackgroundOneByOne", LOG_MESSAGE + "for " + BITMAP_COUNT + " records:" + (new Date().getTime() - start));
    }

    public void onEngineRun(View v) {
        long start = new Date().getTime();
        showAllBitmaps(mPinBuilder.getBitmapArray(BITMAP_COUNT));
        Log.d("onEngineRun", LOG_MESSAGE + "for " + BITMAP_COUNT + " records:" + (new Date().getTime() - start));
    }

    public void showBitmap(Bitmap pin){
        showBitmap(pin, null);
    }

    public void showBitmap(Bitmap pin, LinearLayout imageDisplayArea){
        if(imageDisplayArea == null) imageDisplayArea = (LinearLayout) findViewById(R.id.showing_area);

        ImageView image = new ImageView(this);
        image.setImageBitmap(pin);
        imageDisplayArea.addView(image);
    }

    public void showAllBitmaps(Bitmap[] bitmaps){
        long start = new Date().getTime();
        LinearLayout imageDisplayArea = (LinearLayout) findViewById(R.id.showing_area);
        imageDisplayArea.removeAllViews();

        for(int i = 0; i < bitmaps.length; i++){
//            if(i >= 5 && i < bitmaps.length - 5) continue;
            showBitmap(bitmaps[i], imageDisplayArea);
        }
        Log.d("showAllBitmaps", LOG_MESSAGE + "for " + BITMAP_COUNT + " records:" + (new Date().getTime() - start));
    }
}
