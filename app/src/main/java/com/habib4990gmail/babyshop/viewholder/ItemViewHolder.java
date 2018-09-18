package com.habib4990gmail.babyshop.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.habib4990gmail.babyshop.Interface.ItemClickListener;
import com.habib4990gmail.babyshop.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView item_name;
    public ImageView item_image;

    private ItemClickListener itemClickListener;


    public ItemViewHolder(View itemView) {
        super(itemView);
        item_name = (TextView)itemView.findViewById(R.id.item_name);
        item_image = (ImageView) itemView.findViewById(R.id.item_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
