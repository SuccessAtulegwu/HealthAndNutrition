package com.example.healthandnutrition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    public  static int Splash = 5000;
    ImageView imgSplash;
    TextView txtSplash;
    Animation side,bottom;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        imgSplash = findViewById(R.id.imgSplash);
        txtSplash = findViewById(R.id.txtSplash);

       side = AnimationUtils.loadAnimation(this,R.anim.left_ani);
       bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_ani);
       imgSplash.setAnimation(side);
       txtSplash.setAnimation(bottom);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shared = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime = shared.getBoolean("firstTime", true);

                if(isFirstTime){
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                    startActivity(intent);

                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },Splash);
    }
}