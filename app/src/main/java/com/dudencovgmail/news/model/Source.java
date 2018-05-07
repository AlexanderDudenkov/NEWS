package com.dudencovgmail.news.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Source extends RealmObject {

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}
