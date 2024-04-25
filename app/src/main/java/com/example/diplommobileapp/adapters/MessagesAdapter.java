package com.example.diplommobileapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.chat.ChatViewModel;
import com.example.diplommobileapp.data.models.chat.MessageViewModel;
import com.example.diplommobileapp.viewholders.ChatViewHolder;
import com.example.diplommobileapp.viewholders.MessageViewHolder;

import java.util.Date;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static int SELF_SEND_VIEW_TYPE = 0;
    private static int OTHER_VIEW_TYPE = 1;
    List<MessageViewModel> messages;
    public MessagesAdapter(List<MessageViewModel> messages){
        this.messages = messages;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == SELF_SEND_VIEW_TYPE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item_self, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
        }


        return new MessageViewHolder(view);
    }
    @Override
    public int getItemViewType(int position) {
        MessageViewModel chat = messages.get(position);
        return chat.isSelfSend() ? SELF_SEND_VIEW_TYPE : OTHER_VIEW_TYPE;
    }
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        MessageViewModel chat = messages.get(i);

        messageViewHolder.getMessageTv().setText(chat.getMessage());

        if (chat.isSend()){
            ProgressBar progressBar = messageViewHolder.getLoadingBar();
            if (progressBar!=null){
                progressBar.setVisibility(View.GONE);
                messageViewHolder.getTimeTv().setVisibility(View.VISIBLE);
            }

            Date date = chat.getDateTime();
            if (date==null)
                date = new Date();
            String minutes = date.getMinutes()>9?String.valueOf(date.getMinutes()) :"0"+date.getMinutes();
            String hours = date.getHours()>9?String.valueOf(date.getHours()) :"0"+date.getHours();
            messageViewHolder.getTimeTv().setText(hours+":"+minutes);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
