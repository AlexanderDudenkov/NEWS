package com.dudencovgmail.news.detail_screen.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dudencovgmail.news.R;
import com.dudencovgmail.news.detail_screen.presenter.DetailPresenter;
import com.dudencovgmail.news.detail_screen.presenter.DetailPresenterIF;
import com.dudencovgmail.news.model.Repository;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DetailActivity extends AppCompatActivity implements DetailActivityIF {
    private static final String TAG = "DetailActivity";
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private static final String EXTRA_SAVED = "com.dudencovgmail.splashes.mFragmentIF.detailScreen.itemMenuSaved";
    private static final String EXTRA_POSITION_RV = "com.dudencovgmail.splashes.mFragmentIF.detailScreen.positionRV";
    private DetailPresenterIF mDetailPresenterIF;
    private DetailFragment mFragment;
    private ViewPager viewPager;
    private int mSize;
    private FloatingActionButton fab;
    private boolean mItemMenuSaved;
    private ShareDialog mShareDialog;

    public static Intent newIntent(int position, Context packageContext, boolean itemMenuSaved) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_SAVED, itemMenuSaved);
        intent.putExtra(EXTRA_POSITION_RV, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        loadIfSwipe();
        clickFab();
    }

    private void init() {
        mItemMenuSaved = getIntent().getBooleanExtra(EXTRA_SAVED, false);
        int position = getIntent().getIntExtra(EXTRA_POSITION_RV, 0);
        viewPager = findViewById(R.id.view_pager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);

        mShareDialog = new ShareDialog(this);
        mDetailPresenterIF = new DetailPresenter(this, new Repository());
        mDetailPresenterIF.getSize(mItemMenuSaved);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                mFragment = DetailFragment.newInstance(position, mItemMenuSaved);
                mFragment.setPresenter(mDetailPresenterIF);
                return mFragment;
            }

            @Override
            public int getCount() {
                return mSize;
            }
        });
        viewPager.setCurrentItem(position);
    }

    @Override
    public void getListSize(int size) {
        mSize = size;

    }

    @Override
    public void hideFab() {
        Log.d(TAG, "hideFab()");
        fab.hide();
    }

    @Override
    public void showFab() {
        Log.d(TAG, "showFab()");
        fab.show();
    }

    @Override
    public void showShareDialog(String urlArticle) {
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setQuote(urlArticle)
                .setContentUrl(Uri.parse(urlArticle))
                .build();
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            mShareDialog.show(linkContent);
        }
    }

    @Override
    public void refreshGallery(File newFile) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Log.d(TAG, "refreshGallery(File newFile); Uri = " + Uri.fromFile(newFile));
        intent.setData(Uri.fromFile(newFile));
        sendBroadcast(intent);
    }

    private void printKeyHash() {
        Log.d(TAG, "printKeyHash()");
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.dudencovgmail.news",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void loadIfSwipe() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mDetailPresenterIF.runLoadData(position, viewPager);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void clickFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(DetailActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                mDetailPresenterIF.clickFabControl();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            mDetailPresenterIF.clickMenuControl();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenterIF.detachActivity();
    }
}
