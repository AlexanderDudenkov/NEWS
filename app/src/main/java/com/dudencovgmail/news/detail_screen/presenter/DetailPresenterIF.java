package com.dudencovgmail.news.detail_screen.presenter;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.dudencovgmail.news.detail_screen.view.DetailFragment;

public interface DetailPresenterIF {
    void attachFragment(DetailFragment fragment);

    void detachActivity();

    void detachFragment();

    void getSize(boolean mItemMenuSaved);

    void getArticle(int position, boolean mItemMenuSaved);

    void loadPhoto(Context context, ImageView imageView, String url);

    void runLoadData(int position, ViewPager viewPager);

    void stopFragment();

    void clickFabControl();

    void clickMenuControl();
}
