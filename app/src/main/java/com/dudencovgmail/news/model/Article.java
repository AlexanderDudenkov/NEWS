package com.dudencovgmail.news.model;

import com.google.gson.annotations.SerializedName;

import java.security.SecureRandom;

import io.realm.RealmObject;

public class Article extends RealmObject {
    private int idArticle = new SecureRandom().nextInt();

    @SerializedName("source")
    private Source source;

    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("url")
    private String urlArticle;

    @SerializedName("urlToImage")
    private String urlImage;

    @SerializedName("publishedAt")
    private String publishedAt;

    private String fileName;

    public long getIdArticle() {
        return idArticle;
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlArticle() {
        return urlArticle;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
