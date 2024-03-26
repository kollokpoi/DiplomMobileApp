package com.example.diplommobileapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.example.diplommobileapp.BuildConfig;
import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.chat.ChatViewModel;
import com.example.diplommobileapp.data.viewModels.ChatsViewModel;
import com.example.diplommobileapp.data.viewModels.ViewModelFactory;
import com.example.diplommobileapp.databinding.ActivityMainBinding;
import com.example.diplommobileapp.services.TcpService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.MapKitFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController controller;
    private ChatsViewModel chatsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent serviceIntent = new Intent(this, TcpService.class);
        startService(serviceIntent);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        controller = navController;
        chatsViewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(ChatsViewModel.class);
    }
    public void openEventFragment(int eventId) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        Bundle args = new Bundle();
        args.putInt("eventId", eventId);

        navController.navigate(R.id.navigation_event,args);
    }
}