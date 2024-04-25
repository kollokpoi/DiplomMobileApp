package com.example.diplommobileapp.ui.main;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.diplommobileapp.BuildConfig;
import com.example.diplommobileapp.MyApplication;
import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.chat.MessageViewModel;
import com.example.diplommobileapp.data.viewModels.ChatsViewModel;
import com.example.diplommobileapp.databinding.ActivityMainBinding;
import com.example.diplommobileapp.services.TcpClient;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.yandex.mapkit.MapKitFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController controller;
    private ChatsViewModel viewModel;
    private TcpClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY);
            MapKitFactory.initialize(this);
        }catch (AssertionError exception){
            exception.printStackTrace();
        }

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        controller = navController;

        viewModel = new ViewModelProvider(this).get(ChatsViewModel.class);

        tcpClient = MyApplication.client;
        new Thread(chatListRunnable).start();
    }
    Runnable chatListRunnable = new Runnable() {
        @Override
        public void run() {
            Gson gsonSerializer = new Gson();
            while (true) {
                try {
                    String messageJson = tcpClient.receiveData();
                    if (messageJson == null) {
                        tcpClient.connectWithRetries();
                        continue;
                    }

                    MessageViewModel messageViewModel = gsonSerializer.fromJson(messageJson, MessageViewModel.class);
                    messageViewModel.setSelfSend(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.addMessage(messageViewModel);
                        }
                    });
                } catch (IOException e) {
                    Log.d("disconnected","Я отсоеденился");
                    tcpClient.connectWithRetries();
                }
            }
        }
    };
    public void openEventFragment(int eventId) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        Bundle args = new Bundle();
        args.putInt("eventId", eventId);

        navController.navigate(R.id.navigation_event,args);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}