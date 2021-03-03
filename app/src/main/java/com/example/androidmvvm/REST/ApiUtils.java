package com.example.androidmvvm.REST;

public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = "https://newsapi.org/v2/";

    public static NewsService getNewsService() {

        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

}