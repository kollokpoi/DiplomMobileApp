package com.example.diplommobileapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.chat.ChatViewModel;
import com.example.diplommobileapp.services.imageworker.ImageUtils;
import com.example.diplommobileapp.ui.ChatActivity;
import com.example.diplommobileapp.viewholders.ChatViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<ChatViewModel> chats;
    private Context context;
    public ChatAdapter(List<ChatViewModel> chats, Context context){
        this.chats = chats;
        this.context = context;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatViewModel chat = chats.get(position);

        if (chat.getLastMessage().length()>40){
            holder.getMessageTv().setText(chat.getLastMessage().substring(0,40)+"...");
        }
        else{
            holder.getMessageTv().setText(chat.getLastMessage());
        }
        holder.getNameTv().setText(chat.getTitle());

        Date date = chat.getLastMessageTime();
        if (date==null)
            date = new Date();
        String minutes = date.getMinutes()>9?String.valueOf(date.getMinutes()) :"0"+date.getMinutes();
        String hours = date.getHours()>9?String.valueOf(date.getHours()) :"0"+date.getHours();
        holder.getTimeTv().setText(hours+":"+minutes);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id",chat.getId());
                context.startActivity(intent);
            }
        });
        byte[] preview = chat.getPreviewImage();
        if (preview!=null)
            ImageUtils.setImageViewFromByteArray(preview, holder.getChatImage());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
