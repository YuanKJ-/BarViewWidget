package com.texchi.car.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ykj on 16/9/6.
 */
public class BarView extends View {


    protected Point centerPoint; //中心点

    /**
     * 内环与外环基线
     */
    protected Paint basicOutsidePaint;
    protected float basicOutsideRadius;
    protected RectF basicOutsideOval; //形状和大小

    protected Paint basicInsidePaint;
    protected float basicInsideRadius;
    protected Shader basicInsideShader;


    protected TextPaint valueTextPaint;
    protected TextPaint timeTextPaint;

    public BarView(Context context) {
        this(context, null);
    }

    public BarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Paint paint = new Paint();
        paint.setStrokeJoin(Paint.Join.ROUND); //平滑效果
        paint.setDither(true); //防抖动
        paint.setAntiAlias(true);  //消除锯齿
        paint.setColor(Color.WHITE);  //设置进度的颜色
        paint.setStyle(Paint.Style.STROKE);  //设置样式

        basicOutsidePaint = new Paint(paint);
        basicOutsidePaint.setStrokeWidth(10); //设置宽度
        basicOutsidePaint.setColor(Color.parseColor("#FFFFFF"));
        basicOutsideRadius = 350+5; //实际半径 ＝ 标注半径 + 环宽度/2

        basicInsidePaint = new Paint(paint);
        basicInsidePaint.setStyle(Paint.Style.FILL);  //设置样式
        basicInsideRadius = 350; //实际半径 ＝ 标注半径 + 环宽度/2

        valueTextPaint = new TextPaint(paint);
        valueTextPaint.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
        valueTextPaint.setTextAlign(Paint.Align.CENTER);
        valueTextPaint.setColor(Color.parseColor("#FF2DFDC5"));
        valueTextPaint.setTextSize(getResources().getDimension(R.dimen.value_text_size));
        valueTextPaint.setStyle(Paint.Style.FILL);

        timeTextPaint = new TextPaint(valueTextPaint);
        timeTextPaint.setColor(Color.WHITE);
        timeTextPaint.setTextSize(getResources().getDimension(R.dimen.time_text_size));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerPoint.x, centerPoint.y, basicInsideRadius, basicInsidePaint);
        canvas.drawArc(basicOutsideOval, 0, 360, false, basicOutsidePaint);

        String val = "120";
        float valueHeight = getTextHeight(val, valueTextPaint);
        canvas.drawText(val, centerPoint.x, centerPoint.y , valueTextPaint);

        String time = "16:30";
        float timeHeight = getTextHeight(time, timeTextPaint);
        canvas.drawText(time, centerPoint.x, centerPoint.y + (int)(timeHeight *2), timeTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (centerPoint == null) {
            int centreX = widthSize / 2;
            int centreY = heightSize / 2;
            centerPoint = new Point(centreX, centreY);
        }

        if (basicOutsideOval == null) {
            basicOutsideOval = new RectF(centerPoint.x - basicOutsideRadius, centerPoint.y - basicOutsideRadius,
                    centerPoint.x + basicOutsideRadius, centerPoint.y + basicOutsideRadius);  //用于定义的圆弧的形状和大小的界限
        }

        if(basicInsideShader == null) {
            int[] colors = {Color.BLACK, Color.parseColor("#FF34CA94")};
            float[] positions = {0.8f, 1};
            basicInsideShader = new RadialGradient(centerPoint.x, centerPoint.y,
                    basicInsideRadius, colors, positions, Shader.TileMode.REPEAT);
            basicInsidePaint.setShader(basicInsideShader);
        }
    }


    //获取文字的高度
    public float getTextHeight(String text, Paint paint) {
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        return textBound.bottom - textBound.top;
    }
}
