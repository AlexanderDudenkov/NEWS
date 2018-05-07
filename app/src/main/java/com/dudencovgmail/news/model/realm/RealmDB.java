package com.dudencovgmail.news.model.realm;


import android.support.annotation.NonNull;

import com.dudencovgmail.news.model.Article;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmDB {
    public static RealmConfiguration configDB;
    private Realm mRealm = Realm.getInstance(getConfig());
    private RealmResults<Article> realmResults;

    public RealmConfiguration getConfig() {
        configDB = new RealmConfiguration.Builder()
                .name("db.realm")
                .build();
        Realm.setDefaultConfiguration(configDB);
        return configDB;
    }

    public void deleteResult(final int position) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults = mRealm.where(Article.class).findAll();
                realmResults.deleteFromRealm(position);
            }
        });
    }

    public RealmResults<Article> readResuls() {
        return mRealm.where(Article.class).findAll();
    }

    public void writeResult(final Article article) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.insert(article);
            }
        });
    }

    public void closeDB() {
        mRealm.close();
    }
}
