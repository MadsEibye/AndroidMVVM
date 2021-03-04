package com.example.androidmvvm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidmvvm.Adapters.RecyclerViewSimpleAdapter;
import com.example.androidmvvm.Models.News;
import com.example.androidmvvm.Models.Paging;
import com.example.androidmvvm.R;
import com.example.androidmvvm.REST.ApiUtils;
import com.example.androidmvvm.REST.NewsService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText searchQuery;
    TextView MainMessage;
    String date;
    String dateString;
    Button button;
    TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchQuery = findViewById(R.id.MainSearch);
        searchQuery.setOnEditorActionListener(editorListener);
        searchQuery.setHintTextColor(getResources().getColor(R.color.white));
        MainMessage = findViewById(R.id.ResponseMessage);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        dateString = dtf.format(now);
        button = findViewById(R.id.DatePicker);
        dateText = findViewById(R.id.dateTextView);
        dateText.setText(dateString);
    }

    //TODO Add date picker to UI so user can choose date for search parameter
    //TODO Show Image of article in Recyclerview

    final Calendar myCalendar = Calendar.getInstance();


    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    public void buttonClicked(View v) {
        // TODO Auto-generated method stub
        new DatePickerDialog(MainActivity.this, datePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "yyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(sdf.format(myCalendar.getTime()));
        dateString = sdf.format(myCalendar.getTime());

    }


    private TextView.OnEditorActionListener editorListener = (v, actionId, event) -> {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                searchForNews();
        }
        return false;
    };

    public void searchForNews() {
        String query = searchQuery.getText().toString().trim();

        if (query == null) {
            Toast.makeText(MainActivity.this, "You have to search for something", Toast.LENGTH_LONG).show();
        } else {
            String q = "https://newsapi.org/v2/everything?q=" + query + "&from=" + dateString + "&sortBy=popularity&apiKey=97ee24f1795348b6a7a1e234a11999d3";
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
                        if (allNews.size() == 0) {
                            MainMessage.setText("No news matched your search");
                        } else {
                            MainMessage.setText("Search successful");
                        }
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

