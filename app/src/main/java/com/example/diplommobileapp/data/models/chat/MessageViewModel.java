package com.example.diplommobileapp.data.models.chat;

import android.util.Log;

import com.example.diplommobileapp.data.viewModels.ChatsViewModel;
import com.example.diplommobileapp.services.DateWorker;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

public class MessageViewModel {
    private int id;
    private int chatId;
    private int divisionId;
    private String message;
    private String dateTime;
    private Date createDate;
    private boolean selfSend = false;

    public boolean isSelfSend() {
        return selfSend;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getChatId() {
        return chatId;
    }
    public int getDivisionId() {
        return divisionId;
    }
    public String getMessage() {
        return message;
    }
    public Date getDateTime()  {
        if (dateTime!=null){
            try {
                return DateWorker.parseDate(dateTime);
            } catch (ParseException e) {
                return null;
            }
        } else if (createDate!=null) {
            return (createDate);
        } else return null;
    }
    public void setDateTime()  {
        this.dateTime = DateWorker.getFullDate(new Date());
        ChatsViewModel.getInstance().updateMessageTime(this);
    }

    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate() {
        this.createDate = new Date();
    }

    public boolean isSend()  {
        return dateTime!=null;
    }

    public void setSelfSend(boolean selfSend) {
        this.selfSend = selfSend;
    }
}
