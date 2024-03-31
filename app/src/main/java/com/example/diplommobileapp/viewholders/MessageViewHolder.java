package com.example.diplommobileapp.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplommobileapp.R;

public class MessageViewHolder extends RecyclerView.ViewHolder{
    private TextView messageTv,timeTv;
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        messageTv = itemView.findViewById(R.id.contentTv);
        timeTv = itemView.findViewById(R.id.timeTv);
    }

    public TextView getMessageTv() {
        return messageTv;
    }
    public TextView getTimeTv() {
        return timeTv;
    }
}
