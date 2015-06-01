package com.rdcmappinrenderingresearch.clzhang.mappinrenderingtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

public class InternMapPinBuilder {
    private Context mContext;
    private LayerDrawable mForsalePinDrawable = null;
    private LayerDrawable mOffmarketPinDrawable = null;
    private BitmapDrawable mRightCompoundImage;
    private Drawable mTintablePortion = null;
    private Drawable mForsaleTintablePortion = null;

    static public InternMapPinBuilder getBuilder(Context context) {
        return new InternMapPinBuilder(context);
    }

    private void initialize(Context context){
        mRightCompoundImage = (BitmapDrawable) context.getResources().getDrawable(R.drawable.arrow_tip_down, null);
        mRightCompoundImage.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        if(mForsalePinDrawable != null) return;
        mForsalePinDrawable = (LayerDrawable) context.getResources().getDrawable(R.drawable.text_pin, null);
        mOffmarketPinDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.offmarket_pin, null);
        mTintablePortion = mForsalePinDrawable.findDrawableByLayerId(R.id.tintable_portion);
        mForsaleTintablePortion = mTintablePortion;
        mTintablePortion.setColorFilter(Color.parseColor("#D92228"), PorterDuff.Mode.MULTIPLY);
    }

    private InternMapPinBuilder(Context context) {
        mContext = context;
        initialize(context);
    }

    public Bitmap[] getBitmapArray(int count){
        Bitmap[] aryResult = new Bitmap[count];
        for(int i = 0; i < count; i++){
            aryResult[i] = getForsaleBitmapIcon("Icon " + i);
        }

        return aryResult;
    }

    public Bitmap getForsaleBitmapIcon(String text){
        int compoundImagePaddingDp = 2;
//        Drawable content = createContent(mContext, text, compoundImagePaddingDp, mRightCompoundImage);
        Drawable content = createContent(mContext, text, compoundImagePaddingDp, null);
        return createPin(mForsalePinDrawable, content);
    }

    public Bitmap getOffmarketBitmapIcon(String text){
        return createPin(mOffmarketPinDrawable, createContent(mContext, text, 0, null));
    }

    private Drawable createContent(Context context, String text, int compoundImagePaddingDp, Drawable rightCompoundImage) {
        TextDrawable textDrawable = new TextDrawable(mContext);
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

}
