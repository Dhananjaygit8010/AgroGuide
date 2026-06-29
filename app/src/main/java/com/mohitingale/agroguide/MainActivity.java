package com.mohitingale.agroguide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class MainActivity extends AppCompatActivity {

    //    creation
    ImageView ivSplashLogo;
    TextView tvSplashTitle,tvSplashSLogan;
    LottieAnimationView lottieload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initialization

        ivSplashLogo=findViewById(R.id.ivSplashLogo);
        tvSplashTitle=findViewById(R.id.tvSplashTitle);
        tvSplashSLogan=findViewById(R.id.tvSplashslogan);

        lottieload=findViewById(R.id.lottieload);

        lottieload.setRepeatCount(LottieDrawable.INFINITE);
        lottieload.playAnimation();

        
//      Classname objectname = new constructorname
        Handler H = new Handler();
        H.postDelayed(new Runnable() {
                          @Override
                          public void run() {
//                              jump from one activity to another then used intent
                              Intent i = new Intent(MainActivity.this, LoginActivity.class);
                              startActivity(i);
                              finish();
                          }
                      },2500);

    }

}
