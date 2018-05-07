package com.dudencovgmail.news.detail_screen.view;


import com.dudencovgmail.news.detail_screen.presenter.DetailPresenterIF;
import com.dudencovgmail.news.model.Article;

public interface DetailFragmentIF {

    void setPresenter(DetailPresenterIF presenterIF);

    void getArticle(Article article);

    void showError();
}
