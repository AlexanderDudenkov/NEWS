package com.dudencovgmail.news.list_screen.view;


import android.content.Context;

import com.dudencovgmail.news.list_screen.presenter.PresenterIF;
import com.dudencovgmail.news.model.Article;

import io.realm.RealmList;
import io.realm.RealmResults;

public interface FragmentIF {

    void setPresenter(PresenterIF presenter);

    void updateView();

    void showItems(RealmList<Article> articles);

    void showItems(RealmResults<Article> articles);

    void showProgress();

    void hideProgress();

    void startIntent(int position, Context context, boolean itemMenuSaved);

    void showError();
}
