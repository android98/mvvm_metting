package com.example.mvvmmeeting.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImageModel extends RealmObject {
    @PrimaryKey
    private int imageId;
        public int  parentId;
    private String imagePath;


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
