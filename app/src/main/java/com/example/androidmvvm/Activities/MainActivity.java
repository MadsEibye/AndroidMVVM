package com.example.androidmvvm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidmvvm.Adapters.RecyclerViewSimpleAdapter;
import com.example.androidmvvm.Models.News;
import com.example.androidmvvm.Models.Paging;
import com.example.androidmvvm.R;
import com.example.androidmvvm.REST.ApiUtils;
import com.example.androidmvvm.REST.NewsService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText searchQuery;
    TextView MainMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchQuery = findViewById(R.id.MainSearch);
        searchQuery.setOnEditorActionListener(editorListener);
        MainMessage = findViewById(R.id.ResponseMessage);
    }

    private TextView.OnEditorActionListener editorListener = (v, actionId, event) -> {
        switch (actionId){
            case EditorInfo.IME_ACTION_SEARCH:
                searchForNews();
        }
        return false;
    };

    public void searchForNews() {
        //String q = searchQuery.getText().toString();
        String q = "https://newsapi.org/v2/everything?q=Apple&from=2021-03-03&sortBy=popularity&apiKey=97ee24f1795348b6a7a1e234a11999d3";
        if (q == null) {
            Toast.makeText(MainActivity.this, "You have to search for something", Toast.LENGTH_LONG).show();
        } else {
            NewsService newsService = ApiUtils.getNewsService();
            Call<Paging> searchForNews = newsService.GetNews(q);
            searchForNews.enqueue(new Callback<Paging>() {
                @Override
                public void onResponse(Call<Paging> call, Response<Paging> response) {
                    if (response.isSuccessful()) {
                        Log.d("Tracks", response.body().toString());
                        List<News> allNews = response.body().getArticles();
                        Log.d("Tracks", allNews.toString());
                        populateRecyclerView(allNews);
                        MainMessage.setText("Search successful");
                        hideKeyboardFrom(MainActivity.this, MainMessage);
                        searchQuery.setText("");

                    }
                }

                @Override
                public void onFailure(Call<Paging> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_LONG).show();
                    Log.d("FAIL", t.toString());
                }
            });
        }
    }


    private void populateRecyclerView(List<News> items) {
        RecyclerView recyclerView = findViewById(R.id.SearchResultRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewSimpleAdapter adapter = new RecyclerViewSimpleAdapter<>(items);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position, item) -> {
            Intent intent = new Intent(MainActivity.this, SingleNewsActivity.class);
            startActivity(intent);
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

