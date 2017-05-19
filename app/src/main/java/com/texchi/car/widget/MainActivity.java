package com.texchi.car.widget;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TBarView barView2;
    private int start = 0;
    Handler handler = new Handler();
    Random random = new Random(100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barView2 = (TBarView) findViewById(R.id.barView);
//        TAdjustView adjustView = new TAdjustView(this);
//        setContentView(adjustView);
//        barView2.setIconAndNextDistance(R.mipmap.ic_launcher, "49.6km");
        handler.post(new Runnable() {
            @Override
            public void run() {
                int speed = random.nextInt(100);
                barView2.setSpeed(""+speed);
                handler.postDelayed(this, 200);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (start == 0) {
                barView2.startAnim();
            } else {
                barView2.endAnim();
            }
            Log.d("test", "onTouchEvent: " + start);
            start = ~start;
        }
        return super.onTouchEvent(event);
    }


}
