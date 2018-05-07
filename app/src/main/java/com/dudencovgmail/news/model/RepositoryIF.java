package com.dudencovgmail.news.model;

import android.content.Context;
import android.widget.ImageView;

import java.io.File;

import io.reactivex.Observable;
import io.realm.RealmList;
import io.realm.RealmResults;

public interface RepositoryIF {

    Observable<Model> getLatestArticles(int currentPage);

    Observable<Model> getPopularArticles(int page);

    Observable<Model> getNewsFor2018(int page);

    Observable<Model> getLatestUaArticles(int page);

    Observable<Model> getScienceArticles(int page);

    RealmList<Article> readFromCash();

    Article readFromCash(int position);

    void writeToCash(RealmList<Article> articles);

    void clearCash();

    void loadPhoto(Context context, ImageView imageView, String url);

    File getImageFile(Article article);

    void deleteImageFile(Article article);

    void writeResult(final Article result);

    RealmResults<Article> readResuls();

    void deleteResult(final int position);

    void closeDB();
}
