package com.softmastersgroup.umo.umoagent.models;

import com.google.gson.annotations.Expose;

public class LoginModel {
    @Expose private LoginData logindata;

    public LoginModel(LoginData logindata) {
        this.logindata = logindata;
    }

    public LoginData getLogindata() {
        return logindata;
    }

    public void setLogindata(LoginData logindata) {
        this.logindata = logindata;
    }
}
