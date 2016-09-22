package com.wei.mobileoffice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by weiyilin on 16/5/24.
 */
public class SplashActivity extends BaseActivity{

    private ImageView splash;
    private TextView author;

    private Context mContext;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mContext = SplashActivity.this;
        init();
    }

    private void init() {
        initAnimation();
        author = (TextView) findViewById(R.id.author);
        splash = (ImageView) findViewById(R.id.splash_img);
        author.setText("Luna");
        splash.startAnimation(animation);
    }

    private void initAnimation() {
        animation= AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
