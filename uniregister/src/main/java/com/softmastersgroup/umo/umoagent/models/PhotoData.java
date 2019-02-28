package com.softmastersgroup.umo.umoagent.models;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class PhotoData {

     private RequestBody name;
    private MultipartBody.Part image;

    public RequestBody getName() {
        return name;
    }

    public void setName(RequestBody name) {
        this.name = name;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }
}
