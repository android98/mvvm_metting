package com.example.mvvmmeeting.Models;

public class MainModel {
    private String search, add;

    public MainModel(String search, String add) {
        this.search = search;
        this.add = add;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }
}
