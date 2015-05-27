package com.rdcmappinrenderingresearch.clzhang.mappinrenderingtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;

public class MapPinBitmapBuilder {
    private Context mContext = null;
    private Bitmap mLeftIcon = null;
    private Bitmap mRightIcon = null;
    private String mText = "";
    private int mTextSizeInSp = 12;
    private int mBackgroundColor = Color.RED;
    private int mTextColor = Color.WHITE;
    private NinePatchDrawable mBackgroundDrawable = null;
    private NinePatchDrawable mBackgroundTopDrawable = null;
    private Paint mPaint = null;

    static public MapPinBitmapBuilder getBuilder(Context context) {
        return new MapPinBitmapBuilder(context);
    }

    private MapPinBitmapBuilder(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSizeInSp,
                mContext.getResources().getDisplayMetrics()));
    }

    public Bitmap getIcon() {

        float textWidth = mPaint.measureText(mText);
        float textHeight = Math.abs(mPaint.ascent()) + mPaint.descent();

        assert mBackgroundDrawable != null;
        assert mBackgroundTopDrawable != null;

        Rect padding = new Rect();
        mBackgroundDrawable.getPadding(padding);

        int iLeftIconWidth = mLeftIcon == null ? 0 : mLeftIcon.getWidth();
        int iLeftIconHeight = mLeftIcon == null ? 0 : mLeftIcon.getHeight();
        int iRightIconWidth = mRightIcon == null ? 0 : mRightIcon.getWidth();
        int iWidth = padding.left + padding.right + Math.round(textWidth) + iLeftIconWidth +
                iRightIconWidth;
        int iHeight = padding.top + padding.bottom + Math.max(Math.round(textHeight),
                iLeftIconHeight);

        Bitmap bitmap = Bitmap.createBitmap(iWidth, iHeight, Bitmap.Config.ARGB_8888);
        mBackgroundDrawable.setBounds(0, 0, iWidth, iHeight);
        Canvas canvas = new Canvas(bitmap);
        mBackgroundDrawable.draw(canvas);

        mBackgroundTopDrawable.setColorFilter(mBackgroundColor, PorterDuff.Mode.MULTIPLY);
        mBackgroundTopDrawable.setBounds(3, 3, iWidth - 6, iHeight - 6);
        mBackgroundTopDrawable.draw(canvas);

        canvas.drawText(mText, padding.left + iLeftIconWidth, padding.top +
                Math.abs(mPaint.ascent()), mPaint);

        if (mLeftIcon != null)
            canvas.drawBitmap(mLeftIcon, padding.left, padding.top + iLeftIconHeight / 2, mPaint);
        if (mRightIcon != null)
            canvas.drawBitmap(mRightIcon, iWidth - iRightIconWidth - padding.right, padding.top,
                    mPaint);

        return bitmap;
    }

    public MapPinBitmapBuilder setText(String text) {
        mText = text;
        return this;
    }

    public MapPinBitmapBuilder setTextSizeInSp(int textSizeInSp) {
        mTextSizeInSp = textSizeInSp;
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSizeInSp,
                mContext.getResources().getDisplayMetrics()));
        return this;
    }

    public MapPinBitmapBuilder setLeftIcon(Bitmap icon) {
        mLeftIcon = icon;
        return this;
    }

    public MapPinBitmapBuilder setRightIcon(Bitmap icon) {
        mRightIcon = icon;
        return this;
    }

    public MapPinBitmapBuilder setTextColor(int color) {
        mTextColor = color;
        mPaint.setColor(mTextColor);
        return this;
    }

    public MapPinBitmapBuilder setBackgroundTopDrawable(NinePatchDrawable backgroundTopDrawable) {
        mBackgroundTopDrawable = backgroundTopDrawable;
        return this;
    }

    public MapPinBitmapBuilder setBackgroundColor(int color) {
        mBackgroundColor = color;
        return this;
    }

    public MapPinBitmapBuilder setBackgroundDrawable(NinePatchDrawable backgroundDrawable) {
        mBackgroundDrawable = backgroundDrawable;
        return this;
    }
}
