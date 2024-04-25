package com.example.diplommobileapp.data.viewModels;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diplommobileapp.data.models.chat.ChatViewModel;
import com.example.diplommobileapp.data.models.chat.MessageViewModel;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsViewModel extends ViewModel {

    private MutableLiveData<List<ChatViewModel>> chats = new MutableLiveData<>();
    private MutableLiveData<MessageViewModel> newMessage = new MutableLiveData<>();
    private MutableLiveData<ChatViewModel> currentChat = new MutableLiveData<>();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();

    private static ChatsViewModel instance;
    public ChatsViewModel() {
        if (chats.getValue()==null)
            getChats();
        instance = this;
    }
    public void getChats() {
        IApi api = RetrofitFactory.getApiService();
        api.GetUserChats().enqueue(new Callback<List<ChatViewModel>>() {
            @Override
            public void onResponse(Call<List<ChatViewModel>> call, Response<List<ChatViewModel>> response) {
                if (response.isSuccessful()) {
                    chats.setValue(response.body());
                } else {
                    isError.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<List<ChatViewModel>> call, Throwable t) {
                isError.setValue(true);
            }
        });
    }
    public static ChatsViewModel getInstance(){
        if (instance==null)
            return new ChatsViewModel();
        else return  instance;
    }
    public LiveData<List<ChatViewModel>> getChatsLiveData() {
        return chats;
    }

    public MutableLiveData<ChatViewModel> getCurrentChat() {
        return currentChat;
    }

    public void addMessage(MessageViewModel message) {
        boolean chatExists = false;
        if (!Objects.requireNonNull(chats.getValue()).isEmpty())
            for (ChatViewModel chat : chats.getValue()) {
                if (chat.getId()==(message.getChatId())) {
                    chat.addMessage(message);
                    chats.setValue(chats.getValue());
                    chatExists = true;
                    if (currentChat.getValue()!=null && chat.getId()==currentChat.getValue().getId()){
                        currentChat.getValue().addMessage(message);
                        currentChat.setValue(currentChat.getValue());
                    }
                    break;
                }
            }

        if (!chatExists) {
            IApi api = RetrofitFactory.getApiService();
            api.GetChat(message.getChatId()).enqueue(new Callback<ChatViewModel>() {
                @Override
                public void onResponse(Call<ChatViewModel> call, Response<ChatViewModel> response) {
                    if (response.isSuccessful()) {
                        ChatViewModel chat = response.body();
                        chats.getValue().add(chat);
                        chats.setValue(chats.getValue());
                    } else {
                        isError.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<ChatViewModel> call, Throwable t) {
                    isError.setValue(true);
                }
            });
        }
        newMessage.setValue(message);
    }
    public void updateMessageTime(MessageViewModel message){
        Log.d("messageSended",message.getCreateDate().toString());
        ChatViewModel currentChat = this.currentChat.getValue();
        List<MessageViewModel> messages = currentChat.getMessages();
        int i = 0;
        while (i<messages.size()){
            if (messages.get(i).getCreateDate()!=null){
                Log.d("messageDate",messages.get(i).getCreateDate().toString());
                if (messages.get(i).getCreateDate() == message.getCreateDate() && messages.get(i).isSelfSend()){
                    this.currentChat.setValue(currentChat);
                    Log.d("messageFound","messageFound");
                    break;
                }
            }

            i++;
        }
    }

    public LiveData<MessageViewModel> getNewMessageLiveData() {
        return newMessage;
    }

    public void setCurrentChat(ChatViewModel chat){
        currentChat.setValue(chat);
    }

    public LiveData<Boolean> getIsErrorLiveData() {
        return isError;
    }
}
