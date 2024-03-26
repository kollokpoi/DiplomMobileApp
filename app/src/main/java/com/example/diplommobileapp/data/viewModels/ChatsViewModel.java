package com.example.diplommobileapp.data.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diplommobileapp.data.models.chat.ChatViewModel;
import com.example.diplommobileapp.data.models.chat.MessageViewModel;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsViewModel extends ViewModel {

    private MutableLiveData<List<ChatViewModel>> chats = new MutableLiveData<>();
    private MutableLiveData<MessageViewModel> newMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();

    private IApi api;
    public static ChatsViewModel instance;
    private Context context;
    public ChatsViewModel(Context context) {
        api = RetrofitFactory.getApiService(context);
        instance = this;
        getChats();
    }
    public void getChats() {
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

    public LiveData<List<ChatViewModel>> getChatsLiveData() {
        return chats;
    }

    public void addMessage(MessageViewModel message) {
        boolean chatExists = false;
        for (ChatViewModel chat : chats.getValue()) {
            if (chat.getId()==(message.getChatId())) {
                chat.addMessage(message);
                chats.setValue(chats.getValue());
                chatExists = true;
                break;
            }
        }

        if (!chatExists) {
            api.GetChat(message.getChatId()).enqueue(new Callback<ChatViewModel>() {
                @Override
                public void onResponse(Call<ChatViewModel> call, Response<ChatViewModel> response) {
                    if (response.isSuccessful()) {
                        ChatViewModel chat = response.body();
                        chats.getValue().add(chat);
                        chats.setValue(chats.getValue()); // Обновить LiveData
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

    public LiveData<MessageViewModel> getNewMessageLiveData() {
        return newMessage;
    }

    public LiveData<Boolean> getIsErrorLiveData() {
        return isError;
    }
}
