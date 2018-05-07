package com.dudencovgmail.news.list_screen.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.dudencovgmail.news.R;
import com.dudencovgmail.news.detail_screen.view.DetailActivity;
import com.dudencovgmail.news.list_screen.presenter.PresenterIF;
import com.dudencovgmail.news.model.Article;

import io.realm.RealmList;
import io.realm.RealmResults;

public class ListFragment extends Fragment implements FragmentIF {
    private static final String TAG = "ListFragment";
    private static final String ARG_COLUMN = "com.dudencovgmail.splashes_navdr.view.view.column";
    private RecyclerView mRecyclerView;
    private ProgressDialog progressDialog;
    private ListAdapter mListAdapter;
    private PresenterIF mPresenter;
    private View mView;
    private GridLayoutManager mGridLayoutManager;

    public static ListFragment newInstance(int column) {
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN, column);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mView = view;
        init();
        return view;
    }

    public void init() {
        mRecyclerView = mView.findViewById(R.id.gallery_recycler_view);
        mListAdapter = new ListAdapter();
        mListAdapter.setPresenter(mPresenter);
        mPresenter.attachAdapter(mListAdapter);
        mGridLayoutManager = new GridLayoutManager(getContext(),1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        if (isAdded()) {
            mRecyclerView.setAdapter(mListAdapter);
        }
        mPresenter.onNavigationItemSelected();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager glm = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastPosition = glm.findLastVisibleItemPosition();
                mPresenter.loadIfScrolled(lastPosition, dy);
            }
        });

    }

    @Override
    public void setPresenter(PresenterIF presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateView() {
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showItems(RealmList<Article> articles) {
        Log.i(TAG, "showItems(RealmList<Article> articles)");
        mListAdapter.setData(articles);
    }

    public void showItems(RealmResults<Article> articles) {
        Log.i(TAG, "showItems(RealmResults<Article> articles)");
        mListAdapter.setData(articles);
    }

    @Override
    public void showProgress() {
        Log.i(TAG, "showProgress()");
        progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.loading_message));
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
        mPresenter.stopFragment();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        Log.i(TAG, "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        mPresenter.destroyFragment();
    }

    @Override
    public void startIntent(int position, Context context, boolean itemMenuSaved) {
        Intent i = DetailActivity.newIntent(position, context, itemMenuSaved);
        startActivity(i);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
    }
}
