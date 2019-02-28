package com.softmastersgroup.umo.umoagent.models;

import com.google.gson.annotations.Expose;

public class AuthResponse {
    @Expose private String message;
    @Expose private Auth data;

    public AuthResponse(String message, Auth data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Auth getData() {
        return data;
    }

    public void setData(Auth data) {
        this.data = data;
    }
}
