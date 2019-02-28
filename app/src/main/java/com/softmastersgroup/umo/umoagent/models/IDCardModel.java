package com.softmastersgroup.umo.umoagent.models;

import java.io.Serializable;

public class IDCardModel implements Serializable {
    private String id_type="";
    private String id_number="";
    private String image="";
    private boolean taken=false;
    private String image_name="";


    public IDCardModel() {
    }

    public IDCardModel(String id_type, String id_number, String image) {
        this.id_type = id_type;
        this.id_number = id_number;
        this.image = image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}
