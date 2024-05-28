package com.example.diplommobileapp.data.models.measure;

import com.example.diplommobileapp.data.models.division.Division;
import com.google.gson.annotations.SerializedName;

import java.util.Base64;
import java.util.List;

public class MeasureDivisionsInfo {
    private int id;
    private boolean oneTime = true;
    private boolean weekDays = false;
    private String length;
    private String place = "";
    private boolean sameForAll = false;
    private Integer divisionId;
    private Division division;
    private int measureId;
    private Measure measure;
    private List<MeasureDates> measureDates;
    private List<MeasureDays> measureDays;
    @SerializedName("image")
    private String previewImageBase64;

    public int getId() {
        return id;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public boolean isWeekDays() {
        return weekDays;
    }

    public String getLength() {
        return length;
    }

    public String getPlace() {
        return place;
    }

    public boolean isSameForAll() {
        return sameForAll;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public Division getDivision() {
        return division;
    }

    public int getMeasureId() {
        return measureId;
    }

    public Measure getMeasure() {
        return measure;
    }

    public List<MeasureDates> getMeasureDates() {
        return measureDates;
    }

    public List<MeasureDays> getMeasureDays() {
        return measureDays;
    }

    public byte[] getPreviewImage() {
        if (previewImageBase64!=null){
            return  Base64.getDecoder().decode(previewImageBase64);
        }
        return null;
    }
}
