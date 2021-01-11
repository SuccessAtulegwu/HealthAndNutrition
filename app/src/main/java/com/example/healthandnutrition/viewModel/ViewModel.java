package com.example.healthandnutrition.viewModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthandnutrition.R;
import com.example.healthandnutrition.listemers.ItemClickListener;

public class ViewModel extends RecyclerView.ViewHolder implements View.OnClickListener {

  public   ImageView catImage;
    public TextView catName;
    ItemClickListener itemClickListener;

    public ViewModel(@NonNull View itemView) {
        super(itemView);

        catImage = itemView.findViewById(R.id.cat_image);
        catName = itemView.findViewById(R.id.cat_name);
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
