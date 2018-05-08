package com.dudencovgmail.news.list_screen.presenter;


import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.dudencovgmail.news.R;
import com.dudencovgmail.news.list_screen.view.ActivityIF;
import com.dudencovgmail.news.list_screen.view.FragmentIF;
import com.dudencovgmail.news.list_screen.view.ListAdapter;
import com.dudencovgmail.news.list_screen.view.ListFragment;
import com.dudencovgmail.news.list_screen.view.MainActivity;
import com.dudencovgmail.news.model.Article;
import com.dudencovgmail.news.model.Model;
import com.dudencovgmail.news.model.Repository;
import com.dudencovgmail.news.model.RepositoryIF;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.realm.RealmList;

public class Presenter implements PresenterIF {

    private static final String TAG = "Presenter";
    private Boolean amLoading = false;
    private int mPage = 1;
    private FragmentIF mFragmentIF;
    private RepositoryIF mRepositoryIF;
    private RealmList<Article> mArticles = new RealmList<>();
    private RealmList<Article> mCash;
    private CompositeDisposable mCompositeDisposable;
    private int mItemId;
    private ActivityIF mActivityIF;
    private ListAdapter mAdapter;
    private boolean pageEqualsOneItem1, pageEqualsOneItem2, pageEqualsOneItem3, pageEqualsOneItem4,
            pageEqualsOneItem5,loadingCollections, loadingPhotosCollection;

    public Presenter(MainActivity activity, ListFragment fragment, Repository repository) {
        mActivityIF = activity;
        mFragmentIF = fragment;
        mRepositoryIF = repository;
        mFragmentIF.setPresenter(this);
    }

    @Override
    public void attachAdapter(ListAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void detachView() {
        Log.i(TAG, "detachView()");
        mFragmentIF = null;
    }

    @Override
    public void stopFragment() {
        Log.i(TAG, "stopFragment()");
        mCompositeDisposable.clear();
    }

    @Override
    public void destroyFragment() {
        Log.i(TAG, "destroyFragment()");
        mRepositoryIF.closeDB();
        mCompositeDisposable.dispose();
    }

    @Override
    public void getItemId(int id) {
        mItemId = id;
        onNavigationItemSelected();
    }

    @Override
    public void onNavigationItemSelected() {
        Log.i(TAG, "loadPhotos()");
        mCompositeDisposable = new CompositeDisposable();

        if (mItemId == R.id.new_item || mItemId == 0) {
            if (!pageEqualsOneItem1) {
                pageEqualsOneItem1 = true;
                pageEqualsOneItem2 = pageEqualsOneItem3 = pageEqualsOneItem4 = pageEqualsOneItem5
                        = loadingCollections = false;
                mPage = 1;
                mArticles.clear();
                mRepositoryIF.clearCash();
            }
            mCompositeDisposable.add(mRepositoryIF.getLatestArticles(mPage).subscribeWith(getObserver()));
        } else if (mItemId == R.id.popular_item) {
            if (!pageEqualsOneItem2) {
                pageEqualsOneItem2 = true;
                pageEqualsOneItem1 = pageEqualsOneItem3 = pageEqualsOneItem4 = pageEqualsOneItem5
                        = loadingCollections = false;
                mPage = 1;
                mArticles.clear();
                mRepositoryIF.clearCash();
            }
            mCompositeDisposable.add(mRepositoryIF.getPopularArticles(mPage).subscribeWith(getObserver()));
        } else if (mItemId == R.id.ua_item) {
            if (!pageEqualsOneItem3) {
                pageEqualsOneItem3 = true;
                pageEqualsOneItem1 = pageEqualsOneItem2 = pageEqualsOneItem4 = pageEqualsOneItem5
                        = loadingCollections = false;
                mPage = 1;
                mArticles.clear();
                mRepositoryIF.clearCash();
            }
            mCompositeDisposable.add(mRepositoryIF.getLatestUaArticles(mPage).subscribeWith(getObserver()));
        } else if (mItemId == R.id.news2018_item) {
            if (!pageEqualsOneItem4) {
                pageEqualsOneItem4 = loadingCollections = true;
                pageEqualsOneItem1 = pageEqualsOneItem2 = pageEqualsOneItem3 = pageEqualsOneItem5
                        = loadingPhotosCollection = false;
                mPage = 1;
                mArticles.clear();
                mRepositoryIF.clearCash();
            }

            mCompositeDisposable.add(mRepositoryIF.getNewsFor2018(mPage).subscribeWith(getObserver()));
        } else if (mItemId == R.id.science_item) {
            if (!pageEqualsOneItem5) {
                pageEqualsOneItem5 = loadingCollections = true;
                pageEqualsOneItem1 = pageEqualsOneItem2 = pageEqualsOneItem3 = pageEqualsOneItem4
                        = loadingPhotosCollection = false;
                mPage = 1;
                mArticles.clear();
                mRepositoryIF.clearCash();
            }

            mCompositeDisposable.add(mRepositoryIF.getScienceArticles(mPage).subscribeWith(getObserver()));
        } else if (mItemId == R.id.saved_item) {
            pageEqualsOneItem1 = pageEqualsOneItem2 = pageEqualsOneItem3 = pageEqualsOneItem4
                    = pageEqualsOneItem5 = loadingCollections = false;
            mFragmentIF.showItems(mRepositoryIF.readResuls());
        }
    }

    private DisposableObserver<Model> getObserver() {
        if (mArticles.size() == 0) {
            mFragmentIF.showProgress();
        }
        return new DisposableObserver<Model>() {
            @Override
            public void onNext(Model model) {
                mCash = mRepositoryIF.readFromCash();
                if (mCash.size() == mArticles.size()) {
                    mRepositoryIF.writeToCash(model.getArticles());
                }
                mFragmentIF.hideProgress();
                if (mArticles.size() == 0) {
                    mArticles.addAll(model.getArticles());
                    mFragmentIF.showItems(mArticles);
                } else {
                    mArticles.addAll(model.getArticles());
                    amLoading = false;
                    mFragmentIF.showItems(mArticles);
                }
            }

            @Override
            public void onError(Throwable e) {
                mFragmentIF.showError();
                mFragmentIF.hideProgress();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void loadIfScrolled(int lastPosition, int dy) {
        int maxPage = 3000;
        if (dy > 0 || dy < 0) {
            if (!(amLoading) && (dy > 0) && (mPage < maxPage) && lastPosition >= (mArticles.size() - 8)) {
                amLoading = true;
                mPage++;
                onNavigationItemSelected();
            }
        }
    }

    @Override
    public void onClickPresenter(int position, Context context, boolean itemMenuSaved) {
        Log.d(TAG, "onClickPresenter(); loadingPhotosCollection = " + loadingPhotosCollection);
        mFragmentIF.startIntent(position, context, itemMenuSaved);
    }

    @Override
    public void loadPhoto(Context context, ImageView imageView, String url) {
        mRepositoryIF.loadPhoto(context, imageView, url);
    }

    @Override
    public void backPressControl() {
        Log.d(TAG, "backPressControl(); loadingCollections = " + loadingCollections);
        mActivityIF.closeDrawer();
    }

    @Override
    public void deleteArticle(int position) {
        Log.d(TAG, "deleteArticle(int position); position = " + position);
        mRepositoryIF.deleteImageFile(mRepositoryIF.readResuls().get(position));
        mRepositoryIF.deleteResult(position);
    }
}


