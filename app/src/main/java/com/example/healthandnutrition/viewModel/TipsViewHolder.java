package com.example.healthandnutrition.viewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthandnutrition.R;

public class TipsViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView name,desc;

    public TipsViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.imgWater);
        name = itemView.findViewById(R.id.txtPager);
        desc = itemView.findViewById(R.id.txtDesc);
    }


}
