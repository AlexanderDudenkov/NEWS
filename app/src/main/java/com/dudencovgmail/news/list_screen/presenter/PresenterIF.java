package com.dudencovgmail.news.list_screen.presenter;


import android.content.Context;
import android.widget.ImageView;

import com.dudencovgmail.news.list_screen.view.ListAdapter;

public interface PresenterIF {

    void attachAdapter(ListAdapter adapter);

    void detachView();

    void stopFragment();

    void destroyFragment();

    void getItemId(int itemId);

    void onNavigationItemSelected();

    void loadIfScrolled(int lastPosition, int dy);

    void onClickPresenter(int position, Context context, boolean itemMenuSaved);

    void loadPhoto(Context context, ImageView imageView, String url);

    void backPressControl();

    void deleteArticle(int position);
}
