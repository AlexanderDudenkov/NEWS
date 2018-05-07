package com.dudencovgmail.news.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Model extends RealmObject {

    @SerializedName("articles")
    private RealmList<Article> articles;

    public RealmList<Article> getArticles() {
        return articles;
    }
}

