package com.example.diplommobileapp.adapters;

import static com.example.diplommobileapp.services.DateWorker.getShortDate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.measure.Measure;
import com.example.diplommobileapp.data.viewModels.MeasureViewModel;
import com.example.diplommobileapp.services.imageworker.ImageUtils;
import com.example.diplommobileapp.ui.MeasureActivity;
import com.example.diplommobileapp.viewholders.CircledItemRecycleViewHolder;

import java.text.ParseException;
import java.util.List;

public class MeasuresRecyclerAdapter extends RecyclerView.Adapter<CircledItemRecycleViewHolder> {
    List<MeasureViewModel> measures;
    Context context;
    int orientation = LinearLayoutManager.HORIZONTAL;
    public MeasuresRecyclerAdapter(List<MeasureViewModel> measures, Context context,int orientation){
        this.measures = measures;
        this.context = context;
        this.orientation = orientation;
    }

    public MeasuresRecyclerAdapter(List<MeasureViewModel> measures, Context context){
        this.measures = measures;
        this.context = context;
    }
    @NonNull
    @Override
    public CircledItemRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_view,viewGroup,false);
        if (orientation == LinearLayoutManager.HORIZONTAL && measures.size()>1){
            ViewGroup.LayoutParams params = view.getLayoutParams();
            LinearLayout.LayoutParams newParams = new LinearLayout.LayoutParams((int)(viewGroup.getMeasuredWidth() * 0.8),params.height,1f);
            view.setLayoutParams(newParams);
        }


        return new CircledItemRecycleViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull CircledItemRecycleViewHolder measuresRecyclerViewHolder, int i) {
        try{
            if (orientation== LinearLayoutManager.HORIZONTAL && measures.size()>1) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) measuresRecyclerViewHolder.itemView.getLayoutParams();

                layoutParams.leftMargin = dpToPx(10);
                layoutParams.rightMargin = dpToPx(10);

                if (i == 0)
                    layoutParams.leftMargin = 0;
                if (i == measures.size() - 1)
                    layoutParams.rightMargin = 0;
            }
        }
        catch (Exception ex){}

        MeasureViewModel measure = measures.get(i);

        byte[] preview = measure.getIconImage();
        try {
            String date = getShortDate(measure.getDatetime());
            measuresRecyclerViewHolder.getTimeTv().setText(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (preview!=null)
            ImageUtils.setImageViewFromByteArray(preview, measuresRecyclerViewHolder.getImageView());
        measuresRecyclerViewHolder.getNameTv().setText(measure.getEventName());

        measuresRecyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MeasureActivity.class);
                intent.putExtra("id",measure.getId());
                intent.putExtra("name",measure.getEventName());
                if (measure.getIconImage().length<600864){
                    intent.putExtra("Image",measure.getIconImage());
                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return measures.size();
    }
    public int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
