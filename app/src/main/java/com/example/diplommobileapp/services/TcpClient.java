package com.example.diplommobileapp.services;

import android.util.Log;

import com.example.diplommobileapp.data.models.chat.MessageViewModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient {
    private String userCode;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean isRunning = false;
    private boolean tryingToReconnect = false;

    private String serverAddress;
    private int serverPort;

    public TcpClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }
    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void setUserCode(String userCode){
        this.userCode = userCode;
        authentication();
    }

    public void authentication(){
        sendData(userCode);
        isRunning = true;
    }
    public void connectWithRetries() {
        isRunning = false;
        tryingToReconnect = true;
        while (!isRunning) {
            try {
                if (tryingToReconnect){
                    connect();
                    authentication();
                    tryingToReconnect = false;
                    isRunning = true;
                    Log.d("reconnected","Я пересоединился");
                }
                else{

                }
            } catch (IOException e) {
                System.err.println("Failed to connect to the server. Retrying...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
    public String receiveData() throws IOException {
        return reader.readLine();
    }

    public void sendMessage(MessageViewModel message){
        while (!isRunning){
            try {
                Log.d("reconnecting","Я уже переприсоединяюсь");
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }

        Gson gson = new Gson();
        sendData(gson.toJson(message));
    }

    public void sendData(String data) {
        writer.println(data);
    }

    public void disconnect() throws IOException {
        sendData("disconnect");
        isRunning = false;
        socket.close();
    }
}
