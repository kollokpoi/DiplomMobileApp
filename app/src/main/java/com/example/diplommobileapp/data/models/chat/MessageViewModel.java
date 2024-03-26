package com.example.diplommobileapp.data.models.chat;

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
        }
        else{
            return null;
        }
    }
}
