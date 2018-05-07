package com.dudencovgmail.news.model.rest;


import com.dudencovgmail.news.model.Model;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("everything")
    Observable<Model> getLatestArticles(@Query("sources") String sources,
                                        @Query("page") int page,
                                        @Query("pageSize") int pageSize,
                                        @Query("apiKey") String key);

    @GET("everything")
    Observable<Model> getPopularArticles(@Query("sources") String sources,
                                         @Query("sortBy") String popularity,
                                         @Query("page") int page,
                                         @Query("pageSize") int pageSize,
                                         @Query("apiKey") String key);

    @GET("everything")
    Observable<Model> getNews2018Articles(@Query("sources") String sources,
                                          @Query("from") String fromDate,
                                          @Query("to") String toDate,
                                          @Query("page") int page,
                                          @Query("pageSize") int pageSize,
                                          @Query("apiKey") String key);

    @GET("top-headlines")
    Observable<Model> getLatestUaArticles(@Query("country") String country,
                                          @Query("page") int page,
                                          @Query("pageSize") int pageSize,
                                          @Query("apiKey") String key);

    @GET("top-headlines")
    Observable<Model> getScienceArticles(@Query("category") String category,
                                         @Query("page") int page,
                                         @Query("pageSize") int pageSize,
                                         @Query("apiKey") String key);
}
