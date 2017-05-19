package com.texchi.car.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ykj on 16/9/6.
 */
/**
 * canvas.clipPath
 * http://biancheng.dnbcw.info/shouji/364030.html
 * http://bbs.csdn.net/topics/390954941?page=1
 * by change the center point and oval to implement the animation
 * */
public class TBarView extends View {
    private static final String TAG = "TBarView";

    protected float CIRCLE_RADIUS;
    protected float ARC_RADIUS;

    //the white line width 2px
    protected float BASIC_OUTSIDE_STROKE_WIDTH;
    //the white line radius 120px
    protected float BASIC_OUTSIDE_RADIUS;
    //the outermost shine line radius = BASIC_OUTSIDE_RADIUS = 120px
    protected float BASIC_INSIDE_RADIUS;
    //the shine width 18px / 2 = 9
    protected float SHINE_WIDTH;
    //the vertices of circle left margin screen right
    protected float CIRCLE_MARGIN_RIGHT;
    protected float VALUE_TEXT_SIZE;
    protected float TIME_TEXT_SIZE;
    //the value text center margin screen right 88px
    protected float VALUE_TEXT_MARGIN_RIGHT;
    //the time text margin top with value text 16px
    protected float TIME_MARGIN_TOP;

    protected float REMAIN_DISTANCE_MARGIN_BOTTOM;
    protected float REMAIN_TIME_MARGIN_BOTTOM;
    protected float NEXT_DISTANCE_MARGIN_TOP;
    protected float NAV_ICON_SIZE;


    //blank angle of circle, in design is 87°
    //because circle is too big, blank angle need reduce little more, 60°
    protected float CIRCLE_BLANK_ANGLE;
    //blank angle of arc, in design is 324°
    //because circle is too big, blank angle need reduce little more, 290°
    //avoid to be cut off some shine in drawArc
    protected float ARC_BLANK_ANGLE;
    //the angle from start to finish
    protected float SWEEP_ANGLE;
    //start angle, the vertices of right if 0°
    protected float START_ANGLE;


    protected Point centerPoint; //center point of circle

    protected Paint defaultPaint;

    /**
     * paint  oval  radius
     */
    protected Paint basicOutsidePaint;
    protected float basicOutsideRadius;
    protected RectF basicOutsideOval; //shape and size
    protected SweepGradient basicOutsideShader;

    //black background
    protected Paint backgroundPaint;

    protected List<Paint> shinePaints = new ArrayList<Paint>();
    protected List<Float> shineRadius = new ArrayList<Float>();
    protected List<RectF> shineOvals = new ArrayList<RectF>();
    protected List<Integer> shineColors = new ArrayList<Integer>();


    protected TextPaint valueTextPaint;
    protected TextPaint timeTextPaint;
    protected TextPaint nextDistanceTextPaint;
    protected TextPaint remainDistanceTextPaint;
    protected TextPaint remainTimeTextPaint;

    private String speed = "0";
    private String time = "16:00";
    private String nextDistance = "250m";
    private String remainDistance = "49.5km";
    private String remainTime = "12h18m";
    private Bitmap iconBitmap;
    private RectF picRectF;

    private boolean animating = false;
    private boolean navigating = false;

    protected void initDimens(){
        CIRCLE_RADIUS = getResources().getDimension(R.dimen.circle_outside_radius);
        ARC_RADIUS = getResources().getDimension(R.dimen.arc_outside_radius);

        CIRCLE_MARGIN_RIGHT = getResources().getDimension(R.dimen.circle_margin_right);
        BASIC_OUTSIDE_STROKE_WIDTH = getResources().getDimension(R.dimen.circle_outside_stroke_width);
        BASIC_OUTSIDE_RADIUS = CIRCLE_RADIUS;
        BASIC_INSIDE_RADIUS = BASIC_OUTSIDE_RADIUS;
        VALUE_TEXT_SIZE = getResources().getDimension(R.dimen.value_text_size);
        TIME_TEXT_SIZE = getResources().getDimension(R.dimen.time_text_size);
        VALUE_TEXT_MARGIN_RIGHT = getResources().getDimension(R.dimen.value_text_center_margin_right);
        TIME_MARGIN_TOP = getResources().getDimension(R.dimen.time_margin_top);
        REMAIN_DISTANCE_MARGIN_BOTTOM = getResources().getDimension(R.dimen.remain_distance_margin_bottom);
        REMAIN_TIME_MARGIN_BOTTOM = getResources().getDimension(R.dimen.remain_time_margin_bottom);
        NEXT_DISTANCE_MARGIN_TOP = getResources().getDimension(R.dimen.next_distance_margin_top);
        NAV_ICON_SIZE = getResources().getDimension(R.dimen.nav_icon_size);

        SWEEP_ANGLE = 360 - CIRCLE_BLANK_ANGLE;
        START_ANGLE = CIRCLE_BLANK_ANGLE / 2;

        TypedValue outValue = new TypedValue();

        getResources().getValue(R.dimen.circle_blank_angle, outValue, true);
        CIRCLE_BLANK_ANGLE = outValue.getFloat();

        getResources().getValue(R.dimen.arc_blank_angle, outValue, true);
        ARC_BLANK_ANGLE = outValue.getFloat();

        getResources().getValue(R.dimen.shine_width, outValue, true);
        SHINE_WIDTH = outValue.getFloat();
    }

    /**
     * init basic circle or arc paint by default paint
     * */
    protected void initBasicPaint(){
        backgroundPaint = new Paint(defaultPaint);
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backgroundPaint.setColor(getResources().getColor(android.R.color.black));

        basicOutsidePaint = new Paint(defaultPaint);
        //set width
        basicOutsidePaint.setStrokeWidth(BASIC_OUTSIDE_STROKE_WIDTH);
        //actual radius = design radius + stroke width / 2;
        basicOutsideRadius = BASIC_OUTSIDE_RADIUS + BASIC_OUTSIDE_STROKE_WIDTH / 2;

        parseColor((int)SHINE_WIDTH);
        for (int i = 0; i < (int)SHINE_WIDTH; i++) {
            Paint shinePaint = new Paint(defaultPaint);
            shinePaint.setAntiAlias(false);
            shinePaint.setStrokeWidth(2);
            shinePaint.setColor(shineColors.get(i));
            float radius = BASIC_INSIDE_RADIUS - i * 2;
            shinePaints.add(shinePaint);
            shineRadius.add(radius);
        }
    }

    /** gradient color */
    private void parseColor(int step){
        shineColors.clear();
        int oldR = 8 ,oldG =214,oldB =166;
        int newR = 0 ,newG =12, newB =9;
        int oldColor = Color.rgb(oldR,oldG,oldB);  //start of color
        int newColor = Color.rgb(newR,newG,newB);  //end of color

        for(int i = 1 ; i <= step; i++) {
            int r = oldR + (newR - oldR) * i / step;
            int g = oldG + (newG - oldG) * i / step;
            int b = oldB + (newB - oldB) * i / step;
            int tempColor = Color.rgb(r, g, b);
            shineColors.add(tempColor);
        }
    }

    protected void initTextPaint(){
        valueTextPaint = new TextPaint(defaultPaint);
        valueTextPaint.setTypeface(Typeface.create("Roboto Light", Typeface.NORMAL));
        valueTextPaint.setTextAlign(Paint.Align.CENTER);
        valueTextPaint.setColor(getResources().getColor(R.color.value_text_color));
        valueTextPaint.setTextSize(VALUE_TEXT_SIZE);
        valueTextPaint.setStyle(Paint.Style.FILL);

        timeTextPaint = new TextPaint(valueTextPaint);
        timeTextPaint.setColor(Color.WHITE);
        timeTextPaint.setAlpha(204);
        timeTextPaint.setTextSize(TIME_TEXT_SIZE);

        nextDistanceTextPaint = new TextPaint(timeTextPaint);

        remainDistanceTextPaint = new TextPaint(timeTextPaint);

        remainTimeTextPaint = new TextPaint(timeTextPaint);
        remainTimeTextPaint.setColor(getResources().getColor(R.color.value_text_color));
    }

    public TBarView(Context context) {
        this(context, null);
    }

    public TBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDimens();

        Paint paint = new Paint();
        paint.setStrokeJoin(Paint.Join.ROUND); //ping hua xiao guo
        paint.setDither(true); //fang dou dong
        paint.setAntiAlias(true);  //xiao chu ju chi
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

        defaultPaint = new Paint(paint);
        initBasicPaint();
        initTextPaint();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //background
        canvas.drawArc(basicOutsideOval, START_ANGLE, 360, false, backgroundPaint);

        for(int i = 0; i < shinePaints.size(); i++) {
            canvas.drawArc(shineOvals.get(i), START_ANGLE, SWEEP_ANGLE, false, shinePaints.get(i));
        }
        canvas.drawArc(basicOutsideOval, START_ANGLE, SWEEP_ANGLE, false, basicOutsidePaint);

        float valueWidth = getTextWidth(speed, valueTextPaint);
        int textCenterX = (int) (widthSize - VALUE_TEXT_MARGIN_RIGHT);
        canvas.drawText(speed, textCenterX, centerPoint.y, valueTextPaint);

        float timeHeight = getTextHeight(time, timeTextPaint);
        canvas.drawText(time, textCenterX, centerPoint.y + (int) (timeHeight) + TIME_MARGIN_TOP, timeTextPaint);

        if (navigating && !animating) {
            float remainDistanceHeight = getTextHeight(remainDistance, remainDistanceTextPaint);
            canvas.drawText(remainDistance, textCenterX, heightSize - REMAIN_DISTANCE_MARGIN_BOTTOM, remainDistanceTextPaint);

            int remainTimeY = (int) (heightSize - REMAIN_DISTANCE_MARGIN_BOTTOM - remainDistanceHeight - REMAIN_TIME_MARGIN_BOTTOM);
            canvas.drawText(remainTime, textCenterX, remainTimeY, remainTimeTextPaint);

            if(iconBitmap!=null) {
                canvas.drawBitmap(iconBitmap, null, picRectF, defaultPaint);
            }

            float nextDistanceHeight = getTextHeight(nextDistance, nextDistanceTextPaint);
            canvas.drawText(nextDistance, textCenterX, NEXT_DISTANCE_MARGIN_TOP + (int) nextDistanceHeight + NAV_ICON_SIZE, nextDistanceTextPaint);
        }
    }

    private int widthSize;
    private int heightSize;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        initOval();
    }

    protected void initOval(){
        if (centerPoint == null) {
            int centreX = (int) (widthSize - CIRCLE_MARGIN_RIGHT + BASIC_OUTSIDE_RADIUS);
            int centreY = heightSize / 2;
            centerPoint = new Point(centreX, centreY);
        }

        if (basicOutsideOval == null) {
            basicOutsideOval = new RectF(centerPoint.x - basicOutsideRadius, centerPoint.y - basicOutsideRadius,
                    centerPoint.x + basicOutsideRadius, centerPoint.y + basicOutsideRadius);  //用于定义的圆弧的形状和大小的界限

            int startColor = getResources().getColor(R.color.basic_outside_gradient_color);
            int endColor = Color.WHITE;
            int[] colors = new int[]{startColor, endColor, startColor};
            float[] positions = new float[]{45f/360, 233f/360, 1};
            basicOutsideShader = new SweepGradient(centerPoint.x,centerPoint.y, colors, positions);
            basicOutsidePaint.setShader(basicOutsideShader);
        }


        if(shineOvals.size() == 0){
            for(int i = 0; i < shineRadius.size(); i++) {
                RectF innerShadowOval = new RectF(centerPoint.x - shineRadius.get(i), centerPoint.y - shineRadius.get(i),
                        centerPoint.x + shineRadius.get(i), centerPoint.y + shineRadius.get(i));
                shineOvals.add(innerShadowOval);
            }
        }

        if (picRectF == null) {
            picRectF = new RectF(widthSize - VALUE_TEXT_MARGIN_RIGHT - NAV_ICON_SIZE / 2, NEXT_DISTANCE_MARGIN_TOP,
                    widthSize - VALUE_TEXT_MARGIN_RIGHT + NAV_ICON_SIZE / 2, NEXT_DISTANCE_MARGIN_TOP + NAV_ICON_SIZE);
        }
    }

    public void setIconDistanceTime(int icon, String nextDistance, String remainDistance, String remainTime){
        if(iconBitmap!=null) iconBitmap.recycle();
        this.iconBitmap = BitmapFactory.decodeResource(getResources(), icon);
        this.nextDistance = nextDistance;
        this.remainDistance = remainDistance;
        this.remainTime = remainTime;
        refreshView();
    }

    public void setSpeed(String speed) {
        this.speed = speed;
        refreshView();
    }

    public void setTime(String time) {
        this.time = time;
        refreshView();
    }

    private void refreshView() {
        if (!animating) {
            invalidate();
        }
    }

    public void changeMode(float fraction){

        // arc length change
        SWEEP_ANGLE = 360 - CIRCLE_BLANK_ANGLE - (ARC_BLANK_ANGLE - CIRCLE_BLANK_ANGLE) * fraction ;
        START_ANGLE = 180 - SWEEP_ANGLE / 2;

        BASIC_OUTSIDE_RADIUS = CIRCLE_RADIUS + (ARC_RADIUS - CIRCLE_RADIUS) * fraction;
        BASIC_INSIDE_RADIUS = BASIC_OUTSIDE_RADIUS;

        basicOutsideOval = null;
        shineRadius.clear();
        shinePaints.clear();
        shineOvals.clear();
        centerPoint = null;
        initBasicPaint();
        initOval();
        invalidate();
    }

    //get Text Height
    public float getTextHeight(String text, Paint paint) {
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        return textBound.bottom - textBound.top;
    }

    //get Text Width
    private float getTextWidth(String text, Paint paint){
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        return textBound.right - textBound.left;
    }


    private static class BarViewAnimUpdateListener implements ValueAnimator.AnimatorUpdateListener,Animator.AnimatorListener {
        public final static int START = 1;
        public final static int END = 2;

        private WeakReference<TBarView> wBarView;
        private int mMode;

        public BarViewAnimUpdateListener(TBarView barView, int mode){
            wBarView = new WeakReference<TBarView>(barView);
            mMode = mode;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if(wBarView.get()!=null){
                if(mMode == START) {
                    wBarView.get().changeMode(animation.getAnimatedFraction());
                }else {
                    wBarView.get().changeMode(1 - animation.getAnimatedFraction());
                }
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if(wBarView != null) {
                wBarView.get().animating = true;
                wBarView.get().invalidate();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(wBarView != null) {
                wBarView.get().animating = false;
                wBarView.get().invalidate();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    public void startAnim() {
        if (navigating) return;
        navigating = true;
        ValueAnimator anim = ValueAnimator.ofFloat(0f);
        BarViewAnimUpdateListener listener = new BarViewAnimUpdateListener(this, BarViewAnimUpdateListener.START);
        anim.addUpdateListener(listener);
        anim.addListener(listener);
        anim.setDuration(500);
        anim.start();
    }

    public void endAnim() {
        if (!navigating) return;
        navigating = false;
        ValueAnimator anim = ValueAnimator.ofFloat(0f);
        BarViewAnimUpdateListener listener = new BarViewAnimUpdateListener(this, BarViewAnimUpdateListener.END);
        anim.addUpdateListener(listener);
        anim.addListener(listener);
        anim.setDuration(500);
        anim.start();
    }
}
