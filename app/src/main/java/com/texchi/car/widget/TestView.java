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
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ykj on 16/9/6.
 */
public class TestView extends View {


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

    protected Paint basicWhitePaint3;
    protected float basicWhiteRadius3;

    protected Paint basicWhitePaint2;
    protected float basicWhiteRadius2;

    protected Paint basicWhitePaint1;
    protected float basicWhiteRadius1;

    protected TextPaint valueTextPaint;
    protected TextPaint timeTextPaint;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Paint paint = new Paint();
        paint.setStrokeJoin(Paint.Join.ROUND); //平滑效果
        paint.setDither(true); //防抖动
        paint.setAntiAlias(true);  //消除锯齿
        paint.setColor(Color.WHITE);  //设置进度的颜色
        paint.setStyle(Paint.Style.STROKE);  //设置样式

        basicOutsidePaint = new Paint(paint);
        basicOutsidePaint.setStrokeWidth(50); //设置宽度
        basicOutsidePaint.setColor(Color.parseColor("#000000"));
        basicOutsidePaint.setShadowLayer(50, 0, 0, Color.parseColor("#FF0C4B41"));
        basicOutsideRadius = 400+25; //实际半径 ＝ 标注半径 + 环宽度/2

        basicInsidePaint = new Paint(paint);
        basicInsidePaint.setStyle(Paint.Style.FILL);  //设置样式
        basicInsideRadius = 400; //实际半径 ＝ 标注半径 + 环宽度/2

        basicWhitePaint3 = new Paint(paint);
        basicWhitePaint3.setStrokeWidth(4); //设置宽度
        basicWhitePaint3.setColor(Color.parseColor("#FF35363A"));
        basicWhiteRadius3 = 260+2; //实际半径 ＝ 标注半径 + 环宽度/2

        basicWhitePaint2 = new Paint(paint);
        basicWhitePaint2.setStrokeWidth(4); //设置宽度
        basicWhitePaint2.setColor(Color.parseColor("#FF202126"));
        basicWhiteRadius2 = 180+2; //实际半径 ＝ 标注半径 + 环宽度/2

        basicWhitePaint1 = new Paint(paint);
        basicWhitePaint1.setStrokeWidth(4); //设置宽度
        basicWhitePaint1.setColor(Color.parseColor("#FF191A1F"));
        basicWhiteRadius1 = 100+2; //实际半径 ＝ 标注半径 + 环宽度/2

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
        canvas.drawCircle(centerPoint.x, centerPoint.y, basicWhiteRadius3, basicWhitePaint3);
        canvas.drawCircle(centerPoint.x, centerPoint.y, basicWhiteRadius2, basicWhitePaint2);
        canvas.drawCircle(centerPoint.x, centerPoint.y, basicWhiteRadius1, basicWhitePaint1);

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
            int[] colors = {Color.BLACK, Color.parseColor("#FF0B4139")};
            float[] positions = {0.7f, 1};
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
