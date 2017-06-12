package com.texchi.car.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

public class TipTextSwitcher extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private static final String TAG = "TipTextSwitcher";

    private int index = 0;
    private long time = 2000;
    private Context mContext;
    private Handler mHandler;

    private String[] resources = {
            ""
    };

    public TipTextSwitcher(Context context) {
        this(context, null);
    }

    public TipTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mHandler = new Handler();
        this.setFactory(this);
    }

    private void initAnim() {
        this.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.text_switcher_in));
        this.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.text_switcher_out));
    }

    private void clearAnim() {
        this.setInAnimation(null);
        this.setOutAnimation(null);
    }

    public void setResources(String[] res) {
        this.resources = res;
    }

    public void setStayTime(long time) {
        this.time = time;
    }

    public void start() {
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
        if (index > resources.length - 1) {
            //已结束,调用onComplete
            onComplete();
        } else {
            updateText();
            mHandler.postDelayed(nextRunnable, time);
        }
    }

    private void updateText() {
        if (index < resources.length) {
            this.setText(resources[index]);
        } else {
            this.setText("");
        }
    }

    @Override
    public View makeView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflate.inflate(R.layout.ancs_text, null);
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

}