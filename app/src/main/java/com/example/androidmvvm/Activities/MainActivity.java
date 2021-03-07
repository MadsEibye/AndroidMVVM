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
import com.example.androidmvvm.ViewModels.MainViewModel;

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
    String dateString;
    Button button;
    TextView dateText;
    RecyclerView recyclerView;
    MainViewModel MainVM = new MainViewModel();
    static String APIKEY = "97ee24f1795348b6a7a1e234a11999d3";

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
        button.setBackgroundColor(getResources().getColor(R.color.DarkGrey));
        dateText = findViewById(R.id.dateTextView);
        recyclerView = findViewById(R.id.SearchResultRecyclerView);
        dateText.setText(dateString);
    }

    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener datePicker = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    public void buttonClicked(View v) {
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
                String query = searchQuery.getText().toString().trim();
                MainVM.searchForNews(query, searchQuery, MainMessage, MainActivity.this, recyclerView, dateString, APIKEY, getApplicationContext());
        }
        return false;
    };
}

