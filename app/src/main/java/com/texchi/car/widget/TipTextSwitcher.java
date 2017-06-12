package com.texchi.car.widget;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

public class TipTextSwitcher extends TextSwitcher implements ViewSwitcher.ViewFactory, View.OnLayoutChangeListener {
    private static final String TAG = "TipTextSwitcher";

    private int index = 0;
    private long time = 2000;
    private Context mContext;
    private Handler mHandler;
    private int lastRight = 0;

    private Paint mPaint;
    private String mText;
    private List<TextResources> resources;

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.d(TAG, "onLayoutChange() called with: " + "v = [" + v + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "], oldLeft = [" + oldLeft + "], oldTop = [" + oldTop + "], oldRight = [" + oldRight + "], oldBottom = [" + oldBottom + "]");
        if (right == 0) return;
        if (lastRight != right) {
            lastRight = right;
            stop();
            updateAndSplit();
        }
    }

    public TipTextSwitcher(Context context) {
        this(context, null);
    }

    public TipTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mHandler = new Handler();
        this.setFactory(this);
        addOnLayoutChangeListener(this);
    }

    private void initAnim() {
        this.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.text_switcher_in));
        this.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.text_switcher_out));
    }

    private void clearAnim() {
        this.setInAnimation(null);
        this.setOutAnimation(null);
    }

    public void setResourcesText(String text) {
        mText = text;
        stop();
        resources = null;
        updateAndSplit();
    }

    // 重新计算文字
    private void updateAndSplit() {
        if (lastRight == 0) return;
        int firstIndex = resources == null ? 0 : resources.get(index).getFirstIndex();
        resources = splitText(mText.substring(firstIndex), firstIndex, lastRight);
        start();
    }

    public void setStayTime(long time) {
        this.time = time;
    }

    public void start() {
        if(lastRight == 0) return;
        this.clearAnim();
        index = 0;
        updateText();
        initAnim();
        mHandler.postDelayed(nextRunnable, time);
    }

    public void stop() {
        this.clearAnim();
        mHandler.removeCallbacks(nextRunnable);
    }

    private void onComplete() {
        stop();
        if(onCompleteListener != null) {
            onCompleteListener.onComplete();
        }
    }

    private void next() {
        index = index + 1;
        if (index > resources.size() - 1) {
            //已结束,调用onComplete
            onComplete();
        } else {
            updateText();
            mHandler.postDelayed(nextRunnable, time);
        }
    }

    private void updateText() {
        if (index < resources.size()) {
            this.setText(resources.get(index).getText());
        } else {
            this.setText("");
        }
    }

    private List<TextResources> splitText(String text,int sIndex, int width) {
        List<TextResources> list = new ArrayList<>();

        int startIndex = 0;
        int endIndex = 0;
        while (startIndex < text.length()) {
            while (endIndex < text.length()) {
                if (getTextWidth(text, mPaint, startIndex, endIndex) > width) {
                    endIndex--;
                    break;
                } else {
                    endIndex++;
                }
            }
            Log.w("index", "startIndex: "+startIndex +",endIndex: "+endIndex);
            String splitT = text.substring(startIndex, endIndex);
            list.add(new TextResources(startIndex + sIndex, splitT));
            Log.w("widthT", "splitText: " + getTextWidth(splitT, mPaint, 0, splitT.length()));
            Log.w("text", "splitText: "+splitT);
            startIndex = endIndex;
        }
        return list;

    }

    //get Text Width
    private float getTextWidth(String text, Paint paint, int startIndex, int endIndex){
        return paint.measureText(text.substring(startIndex, endIndex));
    }

    @Override
    public View makeView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) inflate.inflate(R.layout.ancs_text, null);
        if(mPaint == null) {
            mPaint = textView.getPaint();
        }
        return textView;
    }


    private Runnable nextRunnable = new Runnable() {
        @Override
        public void run() {
            next();
        }
    };

    private OnCompleteListener onCompleteListener;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public interface OnCompleteListener {
        void onComplete();
    }

    private class TextResources {
        private int firstIndex;
        private String text;

        public TextResources(int index, String text) {
            this.firstIndex = index;
            this.text = text;
        }

        public int getFirstIndex() {
            return firstIndex;
        }

        public void setFirstIndex(int firstIndex) {
            this.firstIndex = firstIndex;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}