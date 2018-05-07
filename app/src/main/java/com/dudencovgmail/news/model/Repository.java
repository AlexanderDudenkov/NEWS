package com.dudencovgmail.news.model;


import android.content.Context;
import android.widget.ImageView;

import com.dudencovgmail.news.model.realm.RealmDB;
import com.dudencovgmail.news.model.rest.NetConnection;

import java.io.File;

import io.reactivex.Observable;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Repository implements RepositoryIF {

    private NetConnection mNetConnection = new NetConnection();
    private LoadPhotoUrl mLoadPhotoUrl = new LoadPhotoUrl();
    private RealmDB mRealmDB = new RealmDB();

    @Override
    public Observable<Model> getLatestArticles(int page) {
        return mNetConnection.getLatestArticles(page);
    }

    @Override
    public Observable<Model> getPopularArticles(int page) {
        return mNetConnection.getPopularArticles(page);
    }

    @Override
    public Observable<Model> getNewsFor2018(int page) {
        return mNetConnection.getNews2018Articles(page);
    }

    @Override
    public Observable<Model> getLatestUaArticles(int page) {
        return mNetConnection.getLatestUaArticles(page);
    }

    @Override
    public Observable<Model> getScienceArticles(int page) {
        return mNetConnection.getScienceArticles(page);
    }

    @Override
    public RealmList<Article> readFromCash() {
        return Cash.getInstance().readFromCash();
    }

    @Override
    public Article readFromCash(int position) {
        return Cash.getInstance().readFromCash(position);
    }

    @Override
    public void writeToCash(RealmList<Article> articles) {
        Cash.getInstance().writeToCash(articles);
    }

    @Override
    public void clearCash() {
        Cash.getInstance().clearCash();
    }

    @Override
    public void loadPhoto(Context context, ImageView imageView, String url) {
        mLoadPhotoUrl.loadPhoto(context, imageView, url);
    }

    @Override
    public File getImageFile(Article article) {
        return mLoadPhotoUrl.getImageFile(article);
    }

    @Override
    public void deleteImageFile(Article article) {
        mLoadPhotoUrl.deleteImage(article);
    }

    @Override
    public void writeResult(Article result) {
        mRealmDB.writeResult(result);
    }

    @Override
    public RealmResults<Article> readResuls() {
        return mRealmDB.readResuls();
    }

    @Override
    public void deleteResult(int position) {
        mRealmDB.deleteResult(position);
    }

    @Override
    public void closeDB() {
        mRealmDB.closeDB();
    }
}
