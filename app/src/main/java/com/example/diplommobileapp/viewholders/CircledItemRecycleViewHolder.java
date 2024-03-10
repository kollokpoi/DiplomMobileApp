package com.example.diplommobileapp.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplommobileapp.R;

public class CircledItemRecycleViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView nameTv;
    private TextView timeTv;

    public CircledItemRecycleViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        imageView = itemView.findViewById(R.id.eventImageView);
        nameTv = itemView.findViewById(R.id.nameTv);
        timeTv = itemView.findViewById(R.id.measureTimeTv);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getNameTv() {
        return nameTv;
    }

    public TextView getTimeTv() {
        return timeTv;
    }
}
