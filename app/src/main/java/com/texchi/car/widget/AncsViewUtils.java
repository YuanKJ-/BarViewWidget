package com.texchi.car.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by ykj on 17/6/9.
 */
public class AncsViewUtils implements TipTextSwitcher.OnCompleteListener {
    private static final String TAG = "AncsViewUtils";

    private final static long STAY_TIME = 3600;

    private Context mContext;
    private FloatView mFloatView;
    private TipTextSwitcher mTextSwitcher;

    public AncsViewUtils(Context context) {
        mContext = context;
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.ancs_view, null);
        mTextSwitcher = (TipTextSwitcher) view.findViewById(R.id.test_text);
        mTextSwitcher.setStayTime(STAY_TIME);
        mFloatView = new FloatView(mContext);
        mFloatView.setDuration(FloatView.LENGTH_ALWAYS);
        mFloatView.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mFloatView.setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.ancs_view_height));
        mFloatView.setView(view);
        mFloatView.setGravity(FloatView.TOP, true);
        mTextSwitcher.setOnCompleteListener(this);

    }

    public void setText(String text) {
        mTextSwitcher.stop();
        mFloatView.hide();
        //设置resource
        text = text.replace("\n", "");
        mTextSwitcher.setResourcesText(text);
        mFloatView.show();
    }

    @Override
    public void onComplete() {
        mFloatView.hide();
    }
}