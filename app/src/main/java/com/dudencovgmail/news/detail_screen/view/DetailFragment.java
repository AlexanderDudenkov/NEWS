package com.dudencovgmail.news.detail_screen.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudencovgmail.news.R;
import com.dudencovgmail.news.detail_screen.presenter.DetailPresenterIF;
import com.dudencovgmail.news.model.Article;

public class DetailFragment extends Fragment implements DetailFragmentIF {
    private static final String TAG = "DetailFragment";
    private static final String ARG_ID = "com.dudencovgmail.news.detail_screen.view.id";
    private static final String ARG_SAVED = "com.dudencovgmail.news.detail_screen.view.mItemMenuSaved";
    private int pos;
    private boolean mItemMenuSaved;
    private DetailPresenterIF mDetailPresenterIF;
    private Article mArticle;
    private TextView tvSource, tvAuthor, tvTitle, tvDescription, tvUrlArticle, tvPublishedAt;

    public static DetailFragment newInstance(int pos, boolean mItemMenuSaved) {
        Bundle args = new Bundle();
        args.putInt(ARG_ID, pos);
        args.putBoolean(ARG_SAVED, mItemMenuSaved);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        pos = getArguments().getInt(ARG_ID);
        mItemMenuSaved = getArguments().getBoolean(ARG_SAVED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_item_detail, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        mDetailPresenterIF.attachFragment(this);
        tvSource = v.findViewById(R.id.tvSource);
        tvAuthor = v.findViewById(R.id.tvAuthor);
        tvTitle = v.findViewById(R.id.tvTitle);
        tvDescription = v.findViewById(R.id.tvDescription);
        tvUrlArticle = v.findViewById(R.id.tvUrlArticle);
        tvPublishedAt = v.findViewById(R.id.tvPublishedAt);
        ImageView ivPhotoPager = v.findViewById(R.id.ivItem);
        mDetailPresenterIF.getArticle(pos, mItemMenuSaved);

        String urlImage = null;
        if (mArticle != null){
            tvSource.setText(mArticle.getSource().getName());
            tvAuthor.setText(mArticle.getAuthor());
            tvTitle.setText(mArticle.getTitle());
            tvDescription.setText(mArticle.getDescription());
            tvUrlArticle.setText(mArticle.getUrlArticle());
            tvPublishedAt.setText(mArticle.getPublishedAt());
            urlImage = mArticle.getUrlImage();
        }
        mDetailPresenterIF.loadPhoto(getContext(), ivPhotoPager, urlImage);
    }



    @Override
    public void setPresenter(DetailPresenterIF presenter) {
        mDetailPresenterIF = presenter;
    }

    @Override
    public void getArticle(Article article) {
        mArticle = article;
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop()");
        mDetailPresenterIF.stopFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"onDestroyView()");
        mDetailPresenterIF.detachFragment();
    }
}
