package com.example.diplommobileapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.division.Division;
import com.example.diplommobileapp.services.imageworker.ImageUtils;
import com.example.diplommobileapp.ui.DivisionActivity;
import com.example.diplommobileapp.viewholders.CircledItemRecycleViewHolder;

import java.util.List;

public class DivisionRecycleAdapter extends RecyclerView.Adapter<CircledItemRecycleViewHolder> {

    List<Division> divisions;
    Context context;
    public DivisionRecycleAdapter(List<Division> divisions, Context context){
        this.divisions = divisions;
        this.context = context;
    }

    @NonNull
    @Override
    public CircledItemRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView view = (CardView) LayoutInflater.from(context).inflate(R.layout.event_view, viewGroup, false);
        if (divisions.size()>1){
            ViewGroup.LayoutParams params = view.getLayoutParams();
            ViewGroup.LayoutParams newParams = new LinearLayout.LayoutParams((int)(viewGroup.getMeasuredWidth() * 0.8),params.height,1f);
            view.setLayoutParams(newParams);
        }


        return new CircledItemRecycleViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull CircledItemRecycleViewHolder divisionRecycleViewHolder, int i) {
        if (divisions.size()>1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) divisionRecycleViewHolder.itemView.getLayoutParams();

            layoutParams.leftMargin = dpToPx(10);
            layoutParams.rightMargin = dpToPx(10);

            if (i==0)
                layoutParams.leftMargin = 0;
            if (i== divisions.size()-1)
                layoutParams.rightMargin = 0;
        }


        Division division = divisions.get(i);
        byte[] preview = division.getPreviewImage();
        if (preview!=null)
            ImageUtils.setImageViewFromByteArray(preview, divisionRecycleViewHolder.getImageView());
        divisionRecycleViewHolder.getNameTv().setText(division.getName());

        divisionRecycleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DivisionActivity.class);
                intent.putExtra("id",division.getId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return divisions.size();
    }
    public int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
