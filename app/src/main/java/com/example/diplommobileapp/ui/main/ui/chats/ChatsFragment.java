package com.example.diplommobileapp.ui.main.ui.chats;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.diplommobileapp.MyApplication;
import com.example.diplommobileapp.R;
import com.example.diplommobileapp.adapters.ChatAdapter;
import com.example.diplommobileapp.data.models.chat.ChatViewModel;
import com.example.diplommobileapp.data.models.chat.MessageViewModel;
import com.example.diplommobileapp.data.viewModels.ChatsViewModel;
import com.example.diplommobileapp.databinding.FragmentChatsBinding;
import com.example.diplommobileapp.databinding.FragmentHomeBinding;
import com.example.diplommobileapp.services.TcpClient;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatsViewModel chatsViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        chatsViewModel  = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        chatsViewModel.getChatsLiveData().observe(getViewLifecycleOwner(), chatViewModels -> {
            Comparator<ChatViewModel> comparator = new Comparator<ChatViewModel>() {
                @Override
                public int compare(ChatViewModel chat1, ChatViewModel chat2) {
                    return chat2.getLastMessageTime().compareTo(chat1.getLastMessageTime());
                }
            };

            Collections.sort(chatViewModels, comparator);

            ChatAdapter adapter = new ChatAdapter(chatViewModels,getContext());
            binding.recyclerView.setAdapter(adapter);
        });

        chatsViewModel.getNewMessageLiveData().observe(getViewLifecycleOwner(), message -> {

        });

        return root;
    }

    private void showLoading(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.loading.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            }
        });
    }
    private void endLoading(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.loading.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
    private void showFail(){
        endLoading();
        Toast toast = new Toast(getContext());
        toast.setText(R.string.loading_error);
        toast.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}