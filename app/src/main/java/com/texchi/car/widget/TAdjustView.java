package com.texchi.car.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by ykj on 15-9-15.
 */
public class TAdjustView extends View {
    private final static String TAG = TAdjustView.class.getSimpleName();

    private static float BASIC_OUTSIDE_RADIO;
    private static float BASIC_OUTSIDE_STROKE;
    private static float BASIC_INSIDE_RADIO;
    private static float BASIC_INSIDE_STROKE;

    /**
     * 数值变化监听器
     */
    protected AdjustValueListener adjustValueListener;

    protected Point centerPoint; //中心点

    /**
     * 内环与外环基线
     */
    protected Paint basicOutsidePaint;
    protected float basicOutsideRadius;
    protected RectF basicOutsideOval; //形状和大小

    /**
     * 固定值部分
     */
    protected Paint basicInsidePaint;
    protected float basicInsideRadius;
    protected RectF basicInsideOval;
    //动态变化值部分,跟固定值共用radio\oval
    protected Paint variablePaint;

    /**
     * 固定值部分
     */
    protected Paint basicLinePaint;
    protected Point lStartPoint;
    protected Point lEndPoint;
    protected Point rStartPoint;
    protected Point rEndPoint;

    /**
     * 圆弧值
     */
    protected int value = 4;
    protected int minValue = 0;
    protected int maxValue = 8;

    protected int totalTimes = 8; //转盘可转动次数，每次转动最小变化为1


    public TAdjustView(Context context) {
        this(context, null);
    }

    public TAdjustView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TAdjustView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDimens();

        Paint paint = new Paint();
        paint.setStrokeJoin(Paint.Join.ROUND); //平滑效果
        paint.setDither(true); //防抖动
        paint.setAntiAlias(true);  //消除锯齿
        paint.setColor(Color.WHITE);  //设置进度的颜色
        paint.setStyle(Paint.Style.STROKE);  //设置样式

        basicOutsidePaint = new Paint(paint);
        basicOutsidePaint.setStrokeWidth(2); //设置宽度
        basicOutsidePaint.setColor(Color.parseColor("#ffffff"));
        basicOutsideRadius = 330 + 2 / 2; //实际半径 ＝ 标注半径 + 环宽度/2

        basicInsidePaint = new Paint(paint);
        basicInsidePaint.setStrokeWidth(34); //设置宽度
        basicInsidePaint.setColor(Color.parseColor("#4D5655"));
        basicInsideRadius = 284 + 34 / 2;

        variablePaint = new Paint(basicInsidePaint);
        variablePaint.setColor(Color.parseColor("#0AFFC6"));


        basicLinePaint = new Paint(paint);
        basicLinePaint.setStrokeWidth(2);

    }

    protected void initDimens() {
        BASIC_OUTSIDE_RADIO = getResources().getDimension(R.dimen.t_adjust_outside_radio);
        BASIC_INSIDE_RADIO = getResources().getDimension(R.dimen.t_adjust_inside_radio);
        BASIC_OUTSIDE_STROKE = getResources().getDimension(R.dimen.t_adjust_outside_stroke);
        BASIC_INSIDE_STROKE = getResources().getDimension(R.dimen.t_adjust_inside_stroke);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBasicLine(canvas);
        drawVariableLine(canvas);
        drawText(canvas);
    }

    protected void drawText(Canvas canvas) {

    }

    protected void drawBasicLine(Canvas canvas) {
        canvas.drawArc(basicOutsideOval, 225, 90, false, basicOutsidePaint);
        int startAngle = 225 + 5;
        for(int i = 0; i < 8; i++){
            canvas.drawArc(basicInsideOval, startAngle, 9.14f, false, basicInsidePaint);
            startAngle += 10.14;
        }
        canvas.drawLine(lStartPoint.x, lStartPoint.y, lEndPoint.x, lEndPoint.y, basicLinePaint);
        canvas.drawLine(rStartPoint.x, rStartPoint.y, rEndPoint.x, rEndPoint.y,basicLinePaint);
    }

    protected void drawVariableLine(Canvas canvas) {
        int startAngle = 225 + 5;
        for(int i = 0; i < value; i++){
            canvas.drawArc(basicInsideOval, startAngle, 9.14f, false, variablePaint);
            startAngle += 10.14;
        }

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

            double radian = Math.toRadians(45f);
            double sideLen = Math.sin(radian) * 330;

            int lStartX = (int) (centerPoint.x - sideLen);
            int lStartY = (int) (centerPoint.y - sideLen);
            int lEndX = lStartX + 330 - 284 - 17 - 1;
            int lEndY = lStartY + 330 - 284 - 17 - 1;
            lStartPoint = new Point(lStartX, lStartY);
            lEndPoint = new Point(lEndX, lEndY);

            int rStartX = (int) (centerPoint.x + sideLen);
            int rStartY = (int) (centerPoint.y - sideLen);
            int rEndX = rStartX-330+284+17+1;
            int rEndY = rStartY+330-284-17-1;
            rStartPoint = new Point(rStartX, rStartY);
            rEndPoint = new Point(rEndX, rEndY);
        }
        if (basicOutsideOval == null) {
            basicOutsideOval = new RectF(centerPoint.x - basicOutsideRadius, centerPoint.y - basicOutsideRadius,
                    centerPoint.x + basicOutsideRadius, centerPoint.y + basicOutsideRadius);  //用于定义的圆弧的形状和大小的界限
        }
        if (basicInsideOval == null) {
            basicInsideOval = new RectF(centerPoint.x - basicInsideRadius, centerPoint.y - basicInsideRadius,
                    centerPoint.x + basicInsideRadius, centerPoint.y + basicInsideRadius);  //用于定义的圆弧的形状和大小的界限
        }
    }

    /**
     * 调节数值增加
     * 回调监听器传出变化前后的数值
     */
    public void valueAdd() {
        float tmpValue = (float) Math.abs(maxValue-minValue) / totalTimes;
        if (tmpValue < 1) {
            tmpValue = 1;
        }
        int oldValue = getValue();
        int newValue = (int) (oldValue + tmpValue);
        setValue(newValue);
        newValue = value;
        this.postInvalidate();
        if (this.adjustValueListener != null) {
            adjustValueListener.valueChange(oldValue, newValue);
        }
    }

    /**
     * 调节数值减少
     * 回调监听器传出变化前后的数值
     */
    public void valueSubtract() {
        float tmpValue = (float) Math.abs(maxValue-minValue) / totalTimes;
        if (tmpValue < 1) {
            tmpValue = 1;
        }
        int oldValue = getValue();
        int newValue = (int) (oldValue - tmpValue);
        setValue(newValue);
        newValue = value;
        this.postInvalidate();
        if (this.adjustValueListener != null) {
            adjustValueListener.valueChange(oldValue, newValue);
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value >= maxValue) {
            value = maxValue;
        }
        if (value <= minValue) {
            value = minValue;
        }
        this.value = value;
    }


    public int getTotalTimes() {
        return totalTimes;
    }

    public void setAdjustValueListener(AdjustValueListener adjustValueListener) {
        this.adjustValueListener = adjustValueListener;
    }

    /**
     * adjustView数值变动回调接口，旋转后新的数值可以通过实现AdjustValueListener接口获取
     */
    public interface AdjustValueListener {
        void valueChange(int oldValue, int newValue);
    }
}
