package com.texchi.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ykj on 17/5/25.
 */
public class TVolumeBar extends RelativeLayout {

    private TextView volumeText;
    private ImageView muteImg;
    private VolumeProgressBar progressBar;

    public TVolumeBar(Context context) {
        this(context, null);
    }

    public TVolumeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TVolumeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.volume_bar, this);

        volumeText = (TextView) findViewById(R.id.volumeText);
        muteImg = (ImageView) findViewById(R.id.muteImg);
        progressBar = (VolumeProgressBar) findViewById(R.id.progressbar);
        // default max 8
        progressBar.setMax(8);
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
        volumeText.setText(String.valueOf(progress));
        if(progress > 0) {
            muteImg.setVisibility(GONE);
            volumeText.setVisibility(VISIBLE);
        }else {
            muteImg.setVisibility(VISIBLE);
            volumeText.setVisibility(GONE);
        }
    }

    public void setMax(int max) {
        progressBar.setMax(max);
    }

}
