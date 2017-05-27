package com.texchi.car.widget;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TVolumeBar volumeBar;
    private int progress = 0;
    private boolean add = true;
    private FloatView floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        volumeBar = new TVolumeBar(this);

        floatView = new FloatView(this);
//        floatView.setDuration(FloatView.LENGTH_ALWAYS);
        floatView.setView(volumeBar);
        floatView.setWidth(getResources().getDimensionPixelSize(R.dimen.volume_bar_width));
        floatView.setHeight(getResources().getDimensionPixelSize(R.dimen.volume_bar_height));
        floatView.setGravity(Gravity.TOP, false);
        floatView.setOnHideListener(new FloatView.OnHideListener() {
            @Override
            public void onHide() {
                Log.e(TAG, "onHide: ");
            }
        });
        floatView.show();
//        volumeBar = (TVolumeBar) findViewById(R.id.tVolumeBar);
//        assert volumeBar != null;
//        volumeBar.setMax(8);
//        volumeBar.setProgress(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (add) {
                progress++;
                if (progress >= 8) {
                    add = false;
                }
            } else {
                progress--;
                if (progress <= 0) {
                    add = true;
                }
            }
            volumeBar.setProgress(progress);
        }
        floatView.show();
//        floatView.hideImmediate();
//        floatView.hide();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        floatView.hide();
    }
}
