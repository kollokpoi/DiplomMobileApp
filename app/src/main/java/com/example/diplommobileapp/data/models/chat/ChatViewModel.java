package com.example.diplommobileapp.data.models.chat;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class ChatViewModel {
    private int id;
    private String title;
    @SerializedName("image")
    private String imageString;
    private int divisionId;
    private List<MessageViewModel> messages = new ArrayList<>();



    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getLastMessage(){
        if (!messages.isEmpty()){
            return messages.get(messages.size()-1).getMessage();
        }else{
            return "";
        }
    }
    public Date getLastMessageTime(){
        if (!messages.isEmpty()){
            return messages.get(messages.size()-1).getDateTime();
        }else{
            return null;
        }
    }
    public int getDivisionId() {
        return divisionId;
    }
    public List<MessageViewModel> getMessages() {
        return messages;
    }
    public void addMessage(MessageViewModel message){
        messages.add(message);
    }
    public byte[] getPreviewImage() {
        if (imageString!=null){
            return  Base64.getDecoder().decode(imageString);
        }
        return null;
    }
}
