package com.example.mvvmmeeting.Models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class VoiceModel extends RealmObject {


    @PrimaryKey
    private int voiceId;
    private int parentId;

    private String uri, fileName;


    public int getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(int voiceId) {
        this.voiceId = voiceId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
