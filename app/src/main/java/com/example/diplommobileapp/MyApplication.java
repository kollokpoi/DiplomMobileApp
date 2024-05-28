package com.example.diplommobileapp;

import android.app.Application;

import com.example.diplommobileapp.data.models.auth.UserStamp;
import com.example.diplommobileapp.services.TcpClient;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;
import com.google.firebase.FirebaseApp;

import java.io.IOException;

public class MyApplication extends Application {
    public static UserStamp userStamp;
    public static TcpClient client;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        client = new TcpClient(getString(R.string.tcp_ip),3333);
    }

}