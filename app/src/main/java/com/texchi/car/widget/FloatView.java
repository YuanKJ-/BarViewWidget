package com.texchi.car.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class FloatView {

    public static final int TOP = 0x9731;
    public static final int CENTER = 0x9732;
    public static final int BOTTOM = 0x9733;

    public static final int LENGTH_ALWAYS = 0;
    public static final int LENGTH_SHORT = 2;
    public static final int LENGTH_LONG = 4;

    protected Context mContext;
    protected WindowManager.LayoutParams params;
    protected WindowManager mWM;
    protected View mView;
    protected Handler mHandler;
    protected int mDuration = LENGTH_SHORT;

    public FloatView(Context context){
        this.mContext = context;
        mHandler = new Handler();
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.setTitle("FloatView");
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 为悬浮窗设置View
     * @param view 自定义view
     */
    public void setView(View view) {
        if(mView != null && mView.getParent() != null) {
            mWM.removeView(mView);
        }
        mView = view;
    }

    /**
     * 设置悬浮窗宽度
     * @param width 宽度
     */
    public void setWidth(int width) {
        params.width = width;
    }

    /**
     * 设置悬浮窗高度
     * @param height 高度
     */
    public void setHeight(int height) {
        params.height = height;
    }

    /**
     * 自定义透明度,0.0f全透明,1.0f全不透明
     * @param alpha 透明度
     */
    public void setAlpha(float alpha) {
        params.alpha = alpha;
    }

    /**
     * 自定义窗口类型,默认 {@link WindowManager.LayoutParams#TYPE_SYSTEM_OVERLAY}
     * @param type 窗口类型
     */
    public void setType(int type) {
        params.type = type;
    }

    /**
     * 自定义窗口进出场动画
     * @param windowAnimations 动画资源res
     */
    public void setWindowAnimations(int windowAnimations) {
        params.windowAnimations = windowAnimations;
    }

    /**
     * 设置Gravity,已预定义三种Gravity分别是
     * {@link #TOP} 置顶并水平铺满
     * {@link #CENTER} 居中自适应
     * {@link #BOTTOM} 置底并水平铺满
     * 预定义方案可以通过 defaultAnim 决定是否使用默认动画
     * 不使用默认动画可以通过 {@link #setWindowAnimations} 自定义
     * 除了三种预定义方案,还可传入 Gravity 自定义参数进行布局
     * @param gravity 预定义或自定义Gravity
     * @param defaultAnim 是否使用默认动画
     */
    public void setGravity(int gravity, boolean defaultAnim) {
        switch (gravity) {
            case TOP:
                params.gravity = Gravity.FILL_HORIZONTAL | Gravity.TOP;
                if (defaultAnim) {
                    params.windowAnimations = R.style.top_anim_view;
                }
                break;
            case CENTER:
                params.gravity = Gravity.CENTER;
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                if (defaultAnim) {
                    params.windowAnimations = R.style.center_anim_view;
                }
                break;
            case BOTTOM:
                params.gravity = Gravity.FILL_HORIZONTAL | Gravity.BOTTOM;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                if (defaultAnim) {
                    params.windowAnimations = R.style.bottom_anim_view;
                }
                break;
            default:
                params.gravity = gravity;
        }
    }

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * 显示悬浮窗,如果mView还未初始化则不做处理
     * 如果mView已经显示,则只刷新显示时间,未显示添加到mWM
     */
    public void show() {
        if(mView == null) {
            return;
        }
        if (mView.getParent() == null) {
            mWM.addView(mView, params);
        }
        refreshHideTime();
    }

    /**
     * 隐藏悬浮窗,并触发onHide回调
     * 该隐藏方法会触发window退场动画
     * 如果mView的容器退出,建议调用{@link #hideImmediate()}
     * 否则有可能会导致 Activity 出现内存泄漏
     */
    public void hide(){
        if (mView != null && mView.getParent() != null) {
            mWM.removeView(mView);
            if (onHideListener != null) {
                onHideListener.onHide();
            }
        }
    }

    /**
     * 立刻隐藏悬浮窗,触发onHide回调
     * 不会触发window退场动画
     */
    public void hideImmediate(){
        if (mView != null && mView.getParent() != null) {
            params.windowAnimations = 0;
            mWM.updateViewLayout(mView, params);
            mWM.removeView(mView);
            if (onHideListener != null) {
                onHideListener.onHide();
            }
        }
    }

    /**
     * 设置悬浮窗持续显示时间,预定义三种方案
     * {@link #LENGTH_SHORT} 2秒后自动消失
     * {@link #LENGTH_LONG} 4秒后自动消失
     * {@link #LENGTH_ALWAYS} 持续显示悬浮窗,直至调用 hide 或 hideImmediate
     * 除了三种预定义方案,也可自定义显示时间,传入的参数值代表n秒后自动消失
     * @param duration 持续时间
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * 返回悬浮窗持续显示时间
     * @return 持续时间
     */
    public int getDuration() {
        return mDuration;
    }

    // 刷新悬浮窗的消失时间
    private void refreshHideTime() {
        //判断duration，如果大于#LENGTH_ALWAYS 则设置消失时间
        if (mDuration > LENGTH_ALWAYS) {
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, mDuration * 1000);
        }
    }

    private OnHideListener onHideListener;

    public void setOnHideListener(OnHideListener onHideListener) {
        this.onHideListener = onHideListener;
    }

    public interface OnHideListener {
        void onHide();
    }
}