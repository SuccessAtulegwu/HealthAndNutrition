package com.example.healthandnutrition.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.healthandnutrition.R;

public class SlidesAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlidesAdapter(Context context) {
        this.context = context;
    }

    int images[] = {

          R.drawable.splash2,
          R.drawable.splash3,
          R.drawable.splash1
    };

    int headings[] = {
            R.string.first_title,
            R.string.second_title,
            R.string.third_title
    };

    int description[] = {
            R.string.first_desc,
            R.string.second_desc,
            R.string.third_desc
    };



    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides,container,false);

        ImageView imageView = view.findViewById(R.id.slides_image);
        TextView txtHeading = view.findViewById(R.id.slides_heading);
        TextView desc = view.findViewById(R.id.slides_desc);

        imageView.setImageResource(images[position]);
        txtHeading.setText(headings[position]);
        desc.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
