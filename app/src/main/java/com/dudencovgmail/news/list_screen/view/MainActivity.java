package com.dudencovgmail.news.list_screen.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.dudencovgmail.news.R;
import com.dudencovgmail.news.list_screen.presenter.Presenter;
import com.dudencovgmail.news.list_screen.presenter.PresenterIF;
import com.dudencovgmail.news.model.Repository;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityIF {

    private static final String TAG = "MainActivity";
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private PresenterIF mPresenter;
    private int mColumnNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startFragment();
        mPresenter = new Presenter(this, (ListFragment) fragment, new Repository());

    }

    public void startFragment() {
        fragmentManager = getSupportFragmentManager();
        fragment = ListFragment.newInstance(mColumnNumber);
        ((ListFragment) fragment).setPresenter(mPresenter);
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public void closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        mPresenter.backPressControl();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i(TAG, "onNavigationItemSelected(); mFragmentIF =  ");
        mPresenter.getItemId(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
