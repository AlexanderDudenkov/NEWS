package com.dudencovgmail.news.model.rest;

import android.app.Application;

import com.dudencovgmail.news.BuildConfig;
import com.dudencovgmail.news.Util;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApp extends Application{
    private static ApiService sApiService;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build();
        sApiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApi() {
        return sApiService;
    }
}
