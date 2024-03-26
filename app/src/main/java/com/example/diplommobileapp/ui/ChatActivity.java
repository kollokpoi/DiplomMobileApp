package com.example.diplommobileapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.diplommobileapp.R;
import com.example.diplommobileapp.databinding.ActivityChatBinding;
import com.example.diplommobileapp.databinding.ActivityDivisionBinding;
import com.example.diplommobileapp.services.TcpClient;

import java.io.IOException;

public class ChatActivity extends AppCompatActivity {

    private int chatId = 0;
    private TcpClient client;
    ActivityChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chatId = getIntent().getIntExtra("id",0);
    }
}