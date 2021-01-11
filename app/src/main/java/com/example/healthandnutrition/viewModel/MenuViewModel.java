package com.example.healthandnutrition.viewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthandnutrition.R;
import com.example.healthandnutrition.listemers.ItemClickListener;

public class MenuViewModel extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView menuImage;
    public TextView menuName;
    ItemClickListener itemClickListener;

    public MenuViewModel(@NonNull View itemView) {
        super(itemView);
        menuImage = itemView.findViewById(R.id.menu_img);
        menuName = itemView.findViewById(R.id.menu_txt);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition());

    }
}
