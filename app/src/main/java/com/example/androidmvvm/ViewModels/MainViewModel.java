package com.example.androidmvvm.ViewModels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmvvm.Activities.MainActivity;
import com.example.androidmvvm.Activities.SingleNewsActivity;
import com.example.androidmvvm.Adapters.RecyclerViewSimpleAdapter;
import com.example.androidmvvm.Models.News;
import com.example.androidmvvm.Models.Paging;
import com.example.androidmvvm.REST.ApiUtils;
import com.example.androidmvvm.REST.NewsService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel {

    public void searchForNews(String query, EditText searchQuery, TextView MainMessage, Activity activity, RecyclerView recyclerView, String dateString, String APIKEY, Context context) {

        if (query == null) {
            Toast.makeText(activity, "You have to search for something", Toast.LENGTH_LONG).show();
        } else {
            String q = "https://newsapi.org/v2/everything?q=" + query + "&from=" + dateString + "&sortBy=popularity&apiKey="+APIKEY;
        NewsService newsService = ApiUtils.getNewsService();
            Call<Paging> searchForNews = newsService.GetNews(q);
            searchForNews.enqueue(new Callback<Paging>() {
                @Override
                public void onResponse(Call<Paging> call, Response<Paging> response) {
                    if (response.isSuccessful()) {
                        Log.d("Tracks", response.body().toString());
                        List<News> allNews = response.body().getArticles();
                        Log.d("Tracks", allNews.toString());
                        populateRecyclerView(allNews, recyclerView, activity, context);
                        if (allNews.size() == 0) {
                            MainMessage.setText("No news matched your search");
                        } else {
                            MainMessage.setText("Search successful");
                        }
                        hideKeyboardFrom(activity, MainMessage);
                        searchQuery.setText("");
                    }
                }

                @Override
                public void onFailure(Call<Paging> call, Throwable t) {
                    Log.d("FAIL", t.toString());
                }
            });
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void populateRecyclerView(List<News> items, RecyclerView recyclerView, Activity activity, Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerViewSimpleAdapter adapter = new RecyclerViewSimpleAdapter<>(items);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position, item) -> {
            Intent intent = new Intent(activity, SingleNewsActivity.class);
            intent.putExtra("Title",items.get(position).getTitle());
            intent.putExtra("Author",items.get(position).getAuthor());
            intent.putExtra("Description",items.get(position).getDescription());
            intent.putExtra("SourceName",items.get(position).getSource().getName());
            intent.putExtra("SourceId",items.get(position).getSource().getId());
            intent.putExtra("Link",items.get(position).getUrl());
            intent.putExtra("ImageUrl",items.get(position).getUrlToImage());
            activity.startActivity(intent);
        });
    }
}
