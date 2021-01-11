package com.example.healthandnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.healthandnutrition.common.SlidesAdapter;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    SlidesAdapter slidesAdapter;

    TextView[] dots;
    Button get,next;
    int currentPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewPager = findViewById(R.id.v_pager);
        dotsLayout = findViewById(R.id.linear_layout);
        get = findViewById(R.id.get_started);
        next = findViewById(R.id.next);

        slidesAdapter = new SlidesAdapter(this);
        viewPager.setAdapter(slidesAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(onPageChangeListener);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPos+1);
            }
        });
    }

    public void skip(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void addDots(int position) {
        dots = new TextView[3];
        dotsLayout.removeAllViews();
        for (int i=0; i<dots.length; i++){
           dots[i] = new TextView(this);
           dots[i].setText(Html.fromHtml("&#8226;"));
           dots[i].setTextSize(35);
           dotsLayout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
      ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

          }

          @Override
          public void onPageSelected(int position) {
            addDots(position);
              currentPos = position;
              if (position ==0){
                  get.setVisibility(View.INVISIBLE);
              }else if (position==1){
                  get.setVisibility(View.INVISIBLE);
              }else {
                  Animation animation = AnimationUtils.loadAnimation(OnBoarding.this,R.anim.rit);
                  get.setVisibility(View.VISIBLE);
                  get.setAnimation(animation);
                  get.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                          startActivity(intent);
                          finish();
                      }
                  });

              }
          }

          @Override
          public void onPageScrollStateChanged(int state) {

          }
      };

}