package com.hooooong.pholar.view.sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hooooong.pholar.R;
import com.hooooong.pholar.view.home.HomeActivity;

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        new Handler().postDelayed(new Runnable(){
            // 로그인 되어 있는 경우에 homeactivity페이지, 로그인 안되어 있는 경우 signinactivity로
            @Override
            public void run() {
                if(fUser == null) {
                    Intent mainIntent = new Intent(SplashActivity.this, SigninActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
    }
}