package com.dudencovgmail.news.list_screen.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudencovgmail.news.R;
import com.dudencovgmail.news.list_screen.presenter.PresenterIF;
import com.dudencovgmail.news.model.Article;
import com.dudencovgmail.news.model.LoadPhotoUrl;

import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements RealmChangeListener {

    private static final String TAG = "ListAdapter";
    private RealmResults<Article> mRealmResults;
    private RealmList<Article> mRealmList;
    private Context mContext;
    private Article mArticle;
    private LoadPhotoUrl mLoadPhotoUrl;
    private PresenterIF mPresenterIF;
    private Boolean mVisibilityBtn,mItemMenuSaved;
    private ViewHolder mHolder;
    private int mPosition;

    public ListAdapter() {
        mLoadPhotoUrl = new LoadPhotoUrl();
    }

    public void setPresenter(PresenterIF presenter) {
        mPresenterIF = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.article_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mHolder = holder;
        mPosition = position;
        if (mRealmList != null) {
            mItemMenuSaved = false;
            mArticle = mRealmList.get(position);
        } else if (mRealmResults != null) {
            mItemMenuSaved = true;
            mArticle = mRealmResults.get(position);
        }
        holder.bindGalleryItem(mArticle, mContext);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mRealmList != null) {
            size = mRealmList.size();
        } else if (mRealmResults != null) {
            size = mRealmResults.size();
        }
        return size;
    }

    public void setData(RealmList<Article> realmList) {
        mRealmResults = null;
        mRealmList = realmList;
        mVisibilityBtn = false;
        notifyDataSetChanged();
    }

    public void setData(RealmResults<Article> realmResults) {
        mRealmList = null;
        mRealmResults = realmResults;
        mVisibilityBtn = true;
        if (mRealmResults != null && mRealmResults.size() != 0) {
            mRealmResults.addChangeListener(this);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivItem;
        private TextView tvSource, tvAuthor, tvTitle, tvDescription, tvUrlArticle, tvPublishedAt;
        public Button btnRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivItem = itemView.findViewById(R.id.ivItem);
            tvUrlArticle = itemView.findViewById(R.id.tvUrlArticle);
            tvPublishedAt = itemView.findViewById(R.id.tvPublishedAt);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            itemView.setOnClickListener(this);
        }

        public void bindGalleryItem(Article article, Context context) {
            String urlImage = null;
            if (article != null) {
                tvSource.setText(article.getSource().getName());
                tvAuthor.setText(article.getAuthor());
                tvTitle.setText(article.getTitle());
                tvDescription.setText(article.getDescription());
                tvUrlArticle.setText(article.getUrlArticle());
                tvPublishedAt.setText(article.getPublishedAt());
                urlImage = article.getUrlImage();
                if (mVisibilityBtn) {
                    btnRemove.setVisibility(View.VISIBLE);
                } else {
                    btnRemove.setVisibility(View.GONE);
                }
            }
            if (mItemMenuSaved){
                Bitmap image = BitmapFactory.decodeFile(article.getFileName());
                ivItem.setImageBitmap(image);
            }else {
                mPresenterIF.loadPhoto(context, ivItem, urlImage);
            }
            deleteArticle();
        }

        @Override
        public void onClick(View v) {
            mPresenterIF.onClickPresenter(getLayoutPosition(), v.getContext(), mItemMenuSaved);
        }

        private void deleteArticle() {
            mHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "deleteArticle(); position = " + mPosition);
                    mPresenterIF.deleteArticle(getLayoutPosition());
                }
            });
        }
    }

}

