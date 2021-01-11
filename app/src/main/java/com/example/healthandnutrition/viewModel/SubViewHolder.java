package com.example.healthandnutrition.viewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthandnutrition.R;
import com.example.healthandnutrition.listemers.ItemClickListener;

public class SubViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView subImage;
    public TextView subName;
    ItemClickListener itemClickListener;

    public SubViewHolder(@NonNull View itemView) {
        super(itemView);
        subImage = itemView.findViewById(R.id.sub_img);
        subName = itemView.findViewById(R.id.sub_txt);
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
