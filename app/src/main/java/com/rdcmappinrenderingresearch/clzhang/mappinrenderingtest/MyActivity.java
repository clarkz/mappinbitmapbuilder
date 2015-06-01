package com.rdcmappinrenderingresearch.clzhang.mappinrenderingtest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final ImageView image1 = (ImageView) findViewById(R.id.image1);
        final Button button1 = (Button) findViewById(R.id.button1);

        final ImageView image2 = (ImageView) findViewById(R.id.image2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View textView) {
                AsyncTask<Void, Void, Bitmap[]> task = new AsyncTask<Void, Void, Bitmap[]>() {
                    @Override
                    protected Bitmap[] doInBackground(Void... params) {
                        // Forsale
                        BitmapDrawable rightCompoundImage = (BitmapDrawable) getResources().getDrawable(R.drawable.arrow_tip_down);
                        rightCompoundImage.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                        int compoundImagePaddingDp = 2;
                        LayerDrawable pinDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.text_pin);
                        Drawable tintablePortion = pinDrawable.findDrawableByLayerId(R.id.tintable_portion);
                        tintablePortion.setColorFilter(Color.parseColor("#D92228"), PorterDuff.Mode.MULTIPLY);
                        Drawable content = createContent(MyActivity.this, "200K", compoundImagePaddingDp, rightCompoundImage);
                        Bitmap bitmap1 = createPin(pinDrawable, content);

                        // Offmarket
                        pinDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.offmarket_pin);
                        Bitmap bitmap2 = createPin(pinDrawable, createContent(MyActivity.this, "1.3M", 0, null));

                        return new Bitmap[]{bitmap1, bitmap2};
                    }

                    @Override
                    protected void onPostExecute(Bitmap[] bitmap) {
                        image1.setImageBitmap(bitmap[0]);
                        image2.setImageBitmap(bitmap[1]);
                    }
                };
                task.execute();
            }
        });
    }

    private Drawable createContent(Context context, String text, int compoundImagePaddingDp, Drawable rightCompoundImage) {
        TextDrawable textDrawable = new TextDrawable(MyActivity.this);
        textDrawable.setTextColor(Color.WHITE);
        textDrawable.setText(text);

        int betweenPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, compoundImagePaddingDp, context.getResources().getDisplayMetrics());

        if (rightCompoundImage != null) {
            return createLayoutDrawable(new Drawable[]{textDrawable, rightCompoundImage}, LinearLayout.HORIZONTAL, betweenPadding);
        } else {
            return textDrawable;
        }
    }

    private Drawable createLayoutDrawable(Drawable[] drawables, int orientation, int betweenPadding) {
        int beforePadding = 0, afterPadding = 0;
        for (Drawable drawable : drawables) {
            // TODO: if intrinsic returns -1 then we have to explode, because we can't layout non-intrinsics
            if (orientation == LinearLayout.HORIZONTAL) {
                afterPadding += drawable.getIntrinsicWidth() + betweenPadding;
            } else {
                afterPadding += drawable.getIntrinsicHeight() + betweenPadding;
            }
        }
        afterPadding -= betweenPadding;
        for (int i = 0; i < drawables.length; ++i) {
            if (orientation == LinearLayout.HORIZONTAL) {
                int width = drawables[i].getIntrinsicWidth();
                afterPadding -= width + betweenPadding;
                drawables[i] = new InsetDrawable(drawables[i], beforePadding, 0, afterPadding + betweenPadding, 0);
                beforePadding += width + betweenPadding;
            } else {
                int height = drawables[i].getIntrinsicHeight();
                afterPadding -= height + betweenPadding;
                drawables[i] = new InsetDrawable(drawables[i], 0, beforePadding, 0, afterPadding + betweenPadding);
                beforePadding += height + betweenPadding;
            }
        }
        return new LayerDrawable(drawables);
    }

    private Bitmap createPin(LayerDrawable pinDrawable, Drawable content) {
        pinDrawable.setDrawableByLayerId(R.id.text_placeholder, content);

        int width = pinDrawable.getIntrinsicWidth();
        int height = pinDrawable.getIntrinsicHeight();
        pinDrawable.setBounds(0, 0, width, height);

        Bitmap bitmap = Bitmap.createBitmap(width,
                height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        pinDrawable.draw(canvas);
        return bitmap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
