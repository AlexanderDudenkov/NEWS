package com.dudencovgmail.news.model.rest;


import com.dudencovgmail.news.Util;
import com.dudencovgmail.news.model.Model;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetConnection {

    public Observable<Model> getLatestArticles(int page) {
        return ClientApp.getApi().getLatestArticles(Util.SOURCES, page, Util.PAGE_SIZE, Util.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Model> getPopularArticles(int page) {
        return ClientApp.getApi().getPopularArticles(Util.SOURCES, Util.SORT_POP, page, Util.PAGE_SIZE, Util.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Model> getNews2018Articles(int page) {
        return ClientApp.getApi().getNews2018Articles(Util.SOURCES, Util.FROM_DATE, Util.TO_DATE, page, Util.PAGE_SIZE, Util.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Model> getLatestUaArticles(int page) {
        return ClientApp.getApi().getLatestUaArticles(Util.COUNTRY, page, Util.PAGE_SIZE, Util.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Model> getScienceArticles(int page) {
        return ClientApp.getApi().getScienceArticles(Util.CATEGORY, page, Util.PAGE_SIZE, Util.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}


