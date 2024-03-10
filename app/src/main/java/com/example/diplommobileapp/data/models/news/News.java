package com.example.diplommobileapp.data.models.news;

import com.example.diplommobileapp.data.models.event.Event;
import com.example.diplommobileapp.services.DateWorker;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class News {
    private int id;
    private String title;
    private String description;
    private String dateTime;

    private String author;

    @SerializedName("image")
    private String previewImageBase64;
    private List<Section> sections;
    private Integer eventId;
    private Event event;
    private String mimeType;


    //getters
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Date getDateTime() throws ParseException {
        if (dateTime!=null){
            return DateWorker.parseDate(dateTime);
        }
        else{
            return null;
        }
    }
    public String getAuthor() {
        return author;
    }
    public byte[] getImage() {
        if (previewImageBase64!=null){
            return  Base64.getDecoder().decode(previewImageBase64);
        }
        return null;
    }
    public List<Section> getSections() {
        return sections;
    }
    public Integer getEventId() {
        return eventId;
    }
    public Event getEvent() {
        return event;
    }
    public String getMimeType() {
        return mimeType;
    }
}
