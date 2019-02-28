package com.softmastersgroup.umo.umoagent.models;

public class ImageModel {

    private String url;
    private boolean taken = false;
    private String image_name;

    public ImageModel() {
    }

    public ImageModel(String url, boolean taken) {
        this.url = url;
        this.taken = taken;
    }

    public String getUrl() {
        return url;
    }



    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
