package com.polkapolka.team.micro;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * Created by spyua on 2016/8/13.
 */
public class SplashActivity extends Activity {
    ImageView start_icon;
    private MediaPlayer mp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startlayout);

        start_icon=(ImageView)findViewById(R.id.start_icon_img);

        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        start_icon.startAnimation(animRotate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent().setClass(SplashActivity.this, DeviceScanActivity.class));
                finish();
            }
        }, 5000);

    }



}
