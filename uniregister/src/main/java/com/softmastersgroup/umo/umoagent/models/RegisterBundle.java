package com.softmastersgroup.umo.umoagent.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class RegisterBundle implements Serializable {

    @Expose private BioData biodata;
    @Expose private List<Location> location;
    @Expose private LoginData logindata;

    public RegisterBundle() {
    }

    public BioData getBiodata() {
        return biodata;
    }

    public void setBiodata(BioData biodata) {
        this.biodata = biodata;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public LoginData getLogindata() {
        return logindata;
    }

    public void setLogindata(LoginData logindata) {
        this.logindata = logindata;
    }

}
