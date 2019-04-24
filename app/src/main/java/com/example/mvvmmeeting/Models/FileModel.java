package com.example.mvvmmeeting.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FileModel extends RealmObject {

    @PrimaryKey
    private int fileId;
    private int parentId;
    private String fileName, filePath;


    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
