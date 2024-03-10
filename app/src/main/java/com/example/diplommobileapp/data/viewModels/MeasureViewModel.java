package com.example.diplommobileapp.data.viewModels;

import static com.example.diplommobileapp.services.DateWorker.parseDate;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MeasureViewModel {
    private int id;
    @SerializedName("icon")
    private String iconString;
    private String eventName;

    private String dateTime;
    private boolean sameForAll;

    public String getEventName() {
        return eventName;
    }
    public byte[] getIconImage() {
        if (iconString!=null){
            return  Base64.getDecoder().decode(iconString);
        }
        return null;
    }
    public Date getDatetime() throws ParseException {
        return parseDate(dateTime);
    }
    public int getId() {
        return id;
    }
    public boolean isSameForAll() {
        return sameForAll;
    }
}
