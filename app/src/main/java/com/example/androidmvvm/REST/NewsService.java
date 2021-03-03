package com.example.androidmvvm.REST;

import com.example.androidmvvm.Models.Paging;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NewsService {
    @GET
    Call<Paging> GetNews(@Url String url);
}
