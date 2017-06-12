package com.texchi.car.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TVolumeBar volumeBar;
    private int progress = 0;
    private boolean add = true;
    private FloatView floatView;
    private TipTextSwitcher textSwitcher;
    // 要显示的文本
    String[] strs = new String[]
            {
                    "textView如果想要强制换",
                    "行的话,必须先把TextView",
                    "显示方式修改为多行,然后才",
                    "有换行一说。 方法一般",
                    "用两种,一种是在字符串",
                    "里加入\\n,另外一种就",
                    "是把TextView"
            };

    private String[] resources = {
            "静夜思",
            "床前明月光",
            "疑是地上霜",
            "举头望明月",
            "低头思故乡"
    };

    private Handler handler = new Handler();
    private AncsUtils ancsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textSwitcher = (TipTextSwitcher) findViewById(R.id.test_text);

        ancsUtils = new AncsUtils(getApplicationContext());
        ancsUtils.setText("textView如果想要强制换行的话,必须先把TextView显示方式修改为多行,然后才有换行一说。 方法一般用两种,一种是在字符串里加入“\\n”,另外一种就是把TextView");
//        FloatTips floatTips = new FloatTips(this);
//        floatTips.setGravity(FloatTips.TOP);
//        floatTips.setText("呵呵呵呵呵呵呵呵呵呵");
//        floatTips.setDuration(FloatTips.LENGTH_ALWAYS);
//        floatTips.show();

//        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflate.inflate(R.layout.ancs_view, null);
//
//        textSwitcher = (TipTextSwitcher) view.findViewById(R.id.test_text);
//        textSwitcher.setResources(strs);
//        textSwitcher.setStayTime(2600);
//        TextView textView = (TextView) findViewById(R.id.test_text);
//        textView.setText("textView如果想要强制换行的话,必须先把TextView显示方式修改为多行,然后才有换行一说。 方法一般用两种,一种是在字符串里加入“\\n”,另外一种就是把TextView");

//        volumeBar = new TVolumeBar(this);
//
//        floatView = new FloatView(this);
//        floatView.setDuration(FloatView.LENGTH_ALWAYS);
//        floatView.setView(view);
//        floatView.setWidth(getResources().getDimensionPixelSize(R.dimen.volume_bar_width));
//        floatView.setHeight(getResources().getDimensionPixelSize(R.dimen.volume_bar_height));
//        floatView.setGravity(FloatView.TOP, true);
//        floatView.setOnHideListener(new FloatView.OnHideListener() {
//            @Override
//            public void onHide() {
//                Log.e(TAG, "onHide: ");
//            }
//        });
//        textSwitcher.setOnCompleteListener(new TipTextSwitcher.OnCompleteListener() {
//            @Override
//            public void onComplete() {
//                floatView.hide();
//            }
//        });
//        textSwitcher.start();
//        floatView.show();
//        volumeBar = (TVolumeBar) findViewById(R.id.tVolumeBar);
//        assert volumeBar != null;
//        volumeBar.setMax(8);
//        volumeBar.setProgress(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            textSwitcher.stop();
//            floatView.hide();
//            textSwitcher.setResources(resources);
//            floatView.show();
//            textSwitcher.start();
            ancsUtils.setText("不过在项目实践上发现了这个方法存在一些问题。当字符串存在字母数字时，就会有1-2像素的误差。也正是这个误差，导致代码上判断换行错误，使得界面上显示出错。\\n" +
                    "\\n" +
                    "为了解决这个问题，搜到了这篇文章 戳我\\n" +
                    "\\n" +
                    "这篇文章中使用了另外一个方法测量，没有new TextPaint，而是使用了TextView自己的TextPaint，这个Paint通过TextView.getPaint()方法获得。");
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    floatView.setGravity(FloatView.TOP, true);
//                    floatView.show();
//                    textSwitcher.start();
//                }
//            }, 1);

        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            ancsUtils.setText("不过在项目实践上发现了这个方法存在一些问题。当字符串存在字母数字时，就会有1-2像素的误差。也正是这个误差，导致代码上判断换行错误，使得界面上显示出错。\\n" +
                    "\\n" +
                    "为了解决这个问题，搜到了这篇文章 戳我\\n" +
                    "\\n" +
                    "这篇文章中使用了另外一个方法测量，没有new TextPaint，而是使用了TextView自己的TextPaint，这个Paint通过TextView.getPaint()方法获得。");

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        floatView.hide();
    }
}
