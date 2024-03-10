package com.example.diplommobileapp.adapters;

import static com.example.diplommobileapp.services.DateWorker.getShortDate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.event.Event;
import com.example.diplommobileapp.data.viewModels.MeasureViewModel;
import com.example.diplommobileapp.services.imageworker.ImageUtils;
import com.example.diplommobileapp.ui.MeasureActivity;
import com.example.diplommobileapp.ui.main.MainActivity;
import com.example.diplommobileapp.viewholders.CircledItemRecycleViewHolder;

import java.text.ParseException;
import java.util.List;

public class EventsRecycleAdapter extends RecyclerView.Adapter<CircledItemRecycleViewHolder> {
    List<Event> events;
    Context context;
    public EventsRecycleAdapter(List<Event> events, Context context){
        this.events = events;
        this.context = context;
    }
    @NonNull
    @Override
    public CircledItemRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_view,viewGroup,false);

        return new CircledItemRecycleViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull CircledItemRecycleViewHolder ViewHolder, int i) {
        Event event = events.get(i);
        byte[] preview = event.getPreviewImage();
        if (preview!=null)
            ImageUtils.setImageViewFromByteArray(preview, ViewHolder.getImageView());
        ViewHolder.getNameTv().setText(event.getName());

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).openEventFragment(event.getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}