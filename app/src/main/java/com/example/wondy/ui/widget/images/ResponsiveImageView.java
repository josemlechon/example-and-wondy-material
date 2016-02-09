package com.example.wondy.ui.widget.images;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.wondy.R;


/**
 * @author jose m lechon on 08/02/2016
 * @since 1
 * @version 0.1.0
 */
public class ResponsiveImageView extends ImageView {

    private Float mRatio;
    private Float mUseRatio;
    private Boolean mResponsive;

    public ResponsiveImageView(Context context) {
        this(context, null, 0);
    }

    public ResponsiveImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResponsiveImageView(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ResponsiveImageView, 0, 0);
            try {
                mRatio = array.getFloat(R.styleable.ResponsiveImageView_ratio, 0.0f);
                if (mRatio == 0.0f) {
                    mRatio = null;
                }
                mUseRatio = mRatio;
                mResponsive = array.getBoolean(R.styleable.ResponsiveImageView_responsive, false);
            } finally {
                array.recycle();
            }
        }
    }

    /**
     * Sets the aspect ratio of this ImageView
     *
     * @param ratio the aspect ratio to use
     */
    public void setRatio(Float ratio) {
        mRatio = ratio;
        mUseRatio = ratio;
    }

    /**
     * sets the ImageView to have the same aspect ratio as its content
     *
     * @param b whether or not to act responsively
     */
    public void setResponsive(boolean b) {
        mResponsive = b;
        if (!mResponsive) mUseRatio = mRatio;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (mResponsive != null && mResponsive) {
            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), resId, dimensions);
            mUseRatio = (float) dimensions.outWidth / (float) dimensions.outHeight;
            requestLayout();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (mResponsive != null && mResponsive && drawable != null) {
            mUseRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
            requestLayout();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        if (mResponsive != null && mResponsive && bitmap != null && !bitmap.isRecycled()) {
            mUseRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mUseRatio == null) {
            //behave normally without a ratio
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            //set aspect ratio explicitly.

            //first get the actual dimensions and measurement modes:
            float width = MeasureSpec.getSize(widthMeasureSpec);
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

            float height = MeasureSpec.getSize(heightMeasureSpec);
            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);


            //then act according to the measurement modes:
            if (
                    widthMode == MeasureSpec.UNSPECIFIED
                    ) {
                //width is unspecified, so conform width to height
                width = mRatio * height;
            } else if (
                    heightMode == MeasureSpec.UNSPECIFIED
                    ) { //height is unspecified, so conform height to width
                height = width / mRatio;
            } else if (
                    width / height > mRatio //too wide
                            && (widthMode == MeasureSpec.AT_MOST)
                    ) {
                //width is bounded above and too big, so shrink it
                width -= (width - height * mRatio);
            } else if (
                    width / height < mRatio //too tall
                            && (heightMode == MeasureSpec.AT_MOST)
                    ) {
                //height is bounded above and too big, so shrink it
                height -= (height - width / mRatio);
            }

            //then set the measured dimension.
            setMeasuredDimension((int) width, (int) height);
        }
    }

}
