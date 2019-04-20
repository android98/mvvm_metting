package com.example.mvvmmeeting.Models;

import io.realm.RealmObject;

public class ImageModel extends RealmObject {

    private int imageId,parenId;
    private String imagePath;


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getParenId() {
        return parenId;
    }

    public void setParenId(int parenId) {
        this.parenId = parenId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
