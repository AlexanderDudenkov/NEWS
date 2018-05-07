package com.dudencovgmail.news.model;

import io.realm.RealmList;

public class Cash {
    private static final Cash ourInstance = new Cash();
    private static RealmList sElementList = new RealmList();

    public static Cash getInstance() {
        return ourInstance;
    }

    private Cash() {
    }

    public RealmList<Article> readFromCash() {
        return sElementList;
    }

    public Article readFromCash(int position) {
        return (Article) sElementList.get(position);
    }

    public void writeToCash(RealmList<Article> models) {
        sElementList.addAll(models);
    }

    public void clearCash() {
        sElementList.clear();
    }
}
