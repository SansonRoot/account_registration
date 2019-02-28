package com.softmastersgroup.umo.umoagent.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class LoginData implements Serializable {
    @Expose private String login;
    @Expose private String password;


    public LoginData() {
    }

    public LoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
