package com.example.diplommobileapp.data.models.auth;

public class AuthModel {
    private String Phone;
    private String Code;
    private String DeviceToken;

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
