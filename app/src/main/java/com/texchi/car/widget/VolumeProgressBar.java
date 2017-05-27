package com.texchi.car.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by ykj on 17/5/25.
 */
public class VolumeProgressBar extends ProgressBar {
    private static final String TAG = "VolumeProgressBar";

    private float mProgressWidth;
    private float mCurrentWidth;
    private float mCurrentHeight;
    private Paint mPaint;

    public VolumeProgressBar(Context context) {
        this(context, null);
    }

    public VolumeProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDimens();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
    }

    protected void initDimens(){
        mProgressWidth = getResources().getDimension(R.dimen.volume_progress_width);
        mCurrentWidth = getResources().getDimension(R.dimen.current_p_width);
        mCurrentHeight = getResources().getDimension(R.dimen.current_p_height);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null
                && progressDrawable instanceof LayerDrawable) {
            LayerDrawable d = (LayerDrawable) progressDrawable;
            for (int i = 0; i < d.getNumberOfLayers(); i++) {
                d.getDrawable(i).getBounds().top = (int) (mCurrentHeight / 4);
                d.getDrawable(i).getBounds().bottom = d.getDrawable(i)
                        .getBounds().top
                        + (int) (mCurrentHeight / 2);
            }
        } else if (progressDrawable != null) {
            // It's not a layer drawable but we still need to adjust the bounds
            progressDrawable.getBounds().top = (int) (mCurrentHeight / 4);
            progressDrawable.getBounds().bottom = progressDrawable
                    .getBounds().top
                    + (int) (mCurrentHeight / 2);
        }

        super.onDraw(canvas);

        float radio = getProgress() * 1.0f / getMax();
        float left = mProgressWidth * radio - mCurrentWidth;
        canvas.drawRect(left, 0 - mProgressWidth / 2, left + mCurrentWidth, mCurrentHeight, mPaint);
    }

}
