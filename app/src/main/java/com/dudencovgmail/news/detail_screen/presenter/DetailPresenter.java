package com.dudencovgmail.news.detail_screen.presenter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.dudencovgmail.news.detail_screen.view.DetailActivity;
import com.dudencovgmail.news.detail_screen.view.DetailActivityIF;
import com.dudencovgmail.news.detail_screen.view.DetailFragment;
import com.dudencovgmail.news.detail_screen.view.DetailFragmentIF;
import com.dudencovgmail.news.model.Article;
import com.dudencovgmail.news.model.Model;
import com.dudencovgmail.news.model.RepositoryIF;

import java.io.File;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.realm.RealmResults;

public class DetailPresenter implements DetailPresenterIF {

    private static final String TAG = "DetailPresenter";
    private RepositoryIF mRepositoryIF;
    private DetailFragmentIF mDetailFragmentIF;
    private DetailActivityIF mDetailActivityIF;
    private ViewPager mViewPager;
    private int mSize, mPosition;
    private Boolean amLoading = false;
    private Article mArticle;
    private CompositeDisposable mCompositeDisposable;
    private boolean mItemMenuSaved, isSaved;
    private RealmResults<Article> mArticles;

    public DetailPresenter(DetailActivity activity, RepositoryIF repository) {
        mDetailActivityIF = activity;
        mRepositoryIF = repository;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachFragment(DetailFragment fragment) {
        Log.d(TAG, "attachFragment(DetailFragment fragment)");
        mDetailFragmentIF = fragment;
        isSaved = false;
        mDetailActivityIF.hideFab();
    }

    @Override
    public void detachActivity() {
        mRepositoryIF.closeDB();
        mCompositeDisposable.dispose();
        mDetailActivityIF = null;
    }

    @Override
    public void detachFragment() {
        mDetailFragmentIF = null;
    }

    @Override
    public void getSize(boolean itemMenuSaved) {
        mItemMenuSaved = itemMenuSaved;
        if (mItemMenuSaved) {
            mArticles = mRepositoryIF.readResuls();
            mSize = mArticles.size();
        } else {
            mSize = mRepositoryIF.readFromCash().size();
        }
        mDetailActivityIF.getListSize(mSize);
    }

    @Override
    public void loadPhoto(Context context, ImageView imageView, String url) {
        if (mItemMenuSaved) {
            Bitmap image = BitmapFactory.decodeFile(mArticle.getFileName());
            imageView.setImageBitmap(image);
        } else {
            mRepositoryIF.loadPhoto(context, imageView, url);
        }
    }

    @Override
    public void getArticle(int positionDetFrag, boolean itemMenuSaved) {
        mItemMenuSaved = itemMenuSaved;
        if (mItemMenuSaved) {
            mArticle = mArticles.get(positionDetFrag);
        } else {
            mDetailActivityIF.showFab();
            mArticle = mRepositoryIF.readFromCash(positionDetFrag);
        }
        mDetailFragmentIF.getArticle(mArticle);
    }

    @Override
    public void runLoadData(int positionDetAct, final ViewPager viewPager) {
        mViewPager = viewPager;
        mPosition = positionDetAct;
        if (!(amLoading) && positionDetAct >= (mSize - 15)) {
            amLoading = true;
            int page;
            if (positionDetAct < 30) {
                page = 1;
            } else {
                page = (positionDetAct / 30) + 1;
            }
            page++;
            mCompositeDisposable.add(mRepositoryIF.getLatestArticles(page).subscribeWith(getObserver()));
        }
    }

    @Override
    public void stopFragment() {
        mCompositeDisposable.clear();
    }

    @Override
    public void clickFabControl() {
        saveArticle();
    }

    @Override
    public void clickMenuControl() {
        String urlArticle = mRepositoryIF.readFromCash(mPosition).getUrlArticle();
        mDetailActivityIF.showShareDialog(urlArticle);
    }

    private void saveArticle() {
        if (!isSaved) {
            isSaved = true;
            mDetailActivityIF.hideFab();
            Log.d(TAG, "saveArticle(); mPosition = " + mPosition);
            mArticle = mRepositoryIF.readFromCash(mPosition);
            File file = mRepositoryIF.getImageFile(mArticle);
            mArticle.setFileName(file.getAbsolutePath());
            mRepositoryIF.writeResult(mArticle);
            mDetailActivityIF.refreshGallery(file);
        }

    }

    private DisposableObserver<Model> getObserver() {
        return new DisposableObserver<Model>() {
            @Override
            public void onNext(Model model) {
                mRepositoryIF.writeToCash(model.getArticles());
                getSize(mItemMenuSaved);
                mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        amLoading = false;
                    }
                });
                Objects.requireNonNull(mViewPager.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                if (mDetailFragmentIF != null) {
                    mDetailFragmentIF.showError();
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
