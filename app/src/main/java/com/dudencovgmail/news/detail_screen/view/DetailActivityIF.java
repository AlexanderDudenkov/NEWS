package com.dudencovgmail.news.detail_screen.view;


import java.io.File;

public interface DetailActivityIF {
    void getListSize(int size);

    void hideFab();

    void showFab();

    void showShareDialog(String urlArticle);

    void refreshGallery(File newFile);
}
