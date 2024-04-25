package com.example.diplommobileapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.diplommobileapp.MyApplication;
import com.example.diplommobileapp.R;
import com.example.diplommobileapp.adapters.ChatAdapter;
import com.example.diplommobileapp.adapters.MessagesAdapter;
import com.example.diplommobileapp.data.models.chat.ChatViewModel;
import com.example.diplommobileapp.data.models.chat.MessageViewModel;
import com.example.diplommobileapp.data.viewModels.ChatsViewModel;
import com.example.diplommobileapp.databinding.ActivityChatBinding;
import com.example.diplommobileapp.databinding.ActivityDivisionBinding;
import com.example.diplommobileapp.services.TcpClient;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;
import com.example.diplommobileapp.services.imageworker.ImageUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private int chatId;
    private int divisionId;
    private TcpClient client;
    private ChatsViewModel viewModel;
    ActivityChatBinding binding;
    private boolean sendingMessage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        chatId = getIntent().getIntExtra("id",-1);
        divisionId = getIntent().getIntExtra("divisionId",-1);

        IApi api = RetrofitFactory.getApiService();
        viewModel = ChatsViewModel.getInstance();

        if (chatId!=-1){
            api.GetChat(chatId).enqueue(new Callback<ChatViewModel>() {
                @Override
                public void onResponse(Call<ChatViewModel> call, Response<ChatViewModel> response) {
                    if (response.isSuccessful()){
                        setChat(response.body());
                    }else{
                        showFail();
                    }
                }

                @Override
                public void onFailure(Call<ChatViewModel> call, Throwable t) {
                    showFail();
                }
            });

        } else if (divisionId!=-1) {
            api.GetChatByDivision(divisionId).enqueue(new Callback<ChatViewModel>() {
                @Override
                public void onResponse(Call<ChatViewModel> call, Response<ChatViewModel> response) {
                    if (response.isSuccessful()){
                        setChat(response.body());
                    }else{
                        showFail();
                    }
                }

                @Override
                public void onFailure(Call<ChatViewModel> call, Throwable t) {
                    showFail();
                }
            });
        }else{
            showFail();
        }


        client = MyApplication.client;


        viewModel.getCurrentChat().observe(this, chatViewModel -> {
            MessagesAdapter adapter = new MessagesAdapter(chatViewModel.getMessages());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
            binding.recyclerView.setLayoutManager(layoutManager);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.scrollToPosition(adapter.getItemCount()-1);
        });
        binding.sendMessageBtn.setOnClickListener(this::onSendClick);
    }
    private void setChat(ChatViewModel chat){
        runOnUiThread(()->{
            viewModel.setCurrentChat(chat);
            binding.nameTv.setText(chat.getTitle());
            binding.usernameTv.setText(chat.getOpponentName());
            chatId = chat.getId();
            divisionId = chat.getDivisionId();

            byte[] preview = chat.getPreviewImage();
            if (preview!=null)
                ImageUtils.setImageViewFromByteArray(preview, binding.chatImageView);
        });
    }
    public void onSendClick(View view){
        if (!sendingMessage)
            if (binding.mesageEt.getText().length()>0 && !binding.mesageEt.getText().toString().trim().isEmpty()) {
                String messageText = binding.mesageEt.getText().toString();
                messageText = messageText.trim();

                MessageViewModel messageViewModel = new MessageViewModel();
                messageViewModel.setMessage(messageText);
                messageViewModel.setChatId(chatId);
                messageViewModel.setSelfSend(true);
                messageViewModel.setCreateDate();
                viewModel.addMessage(messageViewModel);
                binding.mesageEt.setText("");
                sendingMessage = true;
                SendMessageTask task = new SendMessageTask(messageViewModel, new OnMessageSended() {
                    @Override
                    public void onTaskComplete() {
                        messageViewModel.setDateTime();
                        sendingMessage = false;
                    }
                });
                task.execute();
            }
    }


    private void showFail(){
        Toast toast = new Toast(this);
        toast.setText(R.string.loading_error);
        toast.show();
    }

    public class SendMessageTask extends AsyncTask<Void, Void, String> {
        MessageViewModel messageViewModel;
        OnMessageSended onMessageSended;
        public SendMessageTask(MessageViewModel messageViewModel,OnMessageSended onMessageSended){
            this.messageViewModel = messageViewModel;
            this.onMessageSended = onMessageSended;
        }
        @Override
        protected String doInBackground(Void... voids) {
            client.sendMessage(messageViewModel);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            onMessageSended.onTaskComplete();
        }
    }
    public interface OnMessageSended
    {
        void onTaskComplete();
    }
}