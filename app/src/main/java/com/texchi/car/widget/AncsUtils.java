package com.texchi.car.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ykj on 17/6/9.
 */
public class AncsUtils implements TipTextSwitcher.OnCompleteListener{

    private final static long STAY_TIME = 2600;

    private Context mContext;
    private FloatView mFloatView;
    private TipTextSwitcher mTextSwitcher;
    private Paint mPaint;
    private View view;

    public AncsUtils(Context context) {
        mContext = context;
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(R.layout.ancs_view, null);
        mTextSwitcher = (TipTextSwitcher) view.findViewById(R.id.test_text);
        mTextSwitcher.setStayTime(STAY_TIME);
        mFloatView = new FloatView(mContext);
        mFloatView.setDuration(FloatView.LENGTH_ALWAYS);
        mFloatView.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mFloatView.setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.ancs_view_height));
        mFloatView.setView(view);
        mFloatView.setGravity(FloatView.TOP, true);
        mTextSwitcher.setOnCompleteListener(this);

        TextView textView = (TextView) inflate.inflate(R.layout.ancs_text, null);
        mPaint = textView.getPaint();
    }

    public void setText(final String text) {
        mTextSwitcher.stop();
        mFloatView.hide();
        //分割text,设置resource
        mFloatView.show();
        view.post(new Runnable() {
            @Override
            public void run() {
//                Log.e("result", "" + view.getWidth());
                mTextSwitcher.setResources(splitText(text, view.getWidth()));
                mTextSwitcher.start();
            }
        });
    }

    private String[] splitText(String text, int width) {
        List<String> list = new ArrayList<>();

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
            list.add(splitT);
            Log.w("widthT", "splitText: " + getTextWidth(splitT, mPaint, 0, splitT.length()));
            Log.w("text", "splitText: "+splitT);
            startIndex = endIndex;
        }
        return list.toArray(new String[list.size()]);

    }

    //get Text Width
    private float getTextWidth(String text, Paint paint, int startIndex, int endIndex){
        return paint.measureText(text.substring(startIndex, endIndex));
    }

    @Override
    public void onComplete() {
        mFloatView.hide();
    }


}
