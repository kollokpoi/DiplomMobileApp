package com.example.diplommobileapp.services;

import android.util.Xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean isRunning = false;

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
        isRunning = true;
    }
    public void authentification(String userCode){
        sendData(userCode);
    }
    public String receiveData() throws IOException {
        String data = reader.readLine();
        return data;
    }

    public void sendData(String data) {
        writer.println(data);
    }

    public void disconnect() throws IOException {
        isRunning = false;
        socket.close();
    }
}
