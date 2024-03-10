package com.example.diplommobileapp.data.models.measure;

import com.example.diplommobileapp.data.models.event.Event;
import com.google.gson.annotations.SerializedName;

import java.util.Base64;
import java.util.List;

public class Measure {
    private int id;
    @SerializedName("icon")
    private String previewImageBase64;
    private String name;
    private String place;
    private String description;
    private String length;
    private boolean sameForAll;
    private boolean oneTime;
    private boolean weekDays;
    private int eventId;
    private Event event;
    private List<MeasureDivisionsInfo> measureDivisionsInfos;


    //getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public byte[] getIconImage() {
        if (previewImageBase64!=null){
            return  Base64.getDecoder().decode(previewImageBase64);
        }
        return null;
    }

    public String getPlace() {
        return place;
    }

    public String getDescription() {
        return description;
    }

    public String getLength() {
        return length;
    }

    public boolean isSameForAll() {
        return sameForAll;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public boolean isWeekDays() {
        return weekDays;
    }

    public int getEventId() {
        return eventId;
    }

    public Event getEvent() {
        return event;
    }

    public List<MeasureDivisionsInfo> getMeasureDivisionsInfos() {
        return measureDivisionsInfos;
    }
}
