package com.example.androidmvvm.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidmvvm.R;
import com.squareup.picasso.Picasso;

public class SingleNewsActivity extends AppCompatActivity {

    ImageView imageView;
    String ImageUrl, Title, Author, Description, Link, Name;
    TextView TitleTV, AuthorTv, DescriptionTV, SourceTV, LinkTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);
        Intent intent = getIntent();
        Title = intent.getStringExtra("Title");
        Author = intent.getStringExtra("Author");
        Description = intent.getStringExtra("Description");
        Link = intent.getStringExtra("Link");
        Name = intent.getStringExtra("SourceName");
        ImageUrl = intent.getStringExtra("ImageUrl");
        TitleTV = findViewById(R.id.SingleTitleTextView);
        AuthorTv = findViewById(R.id.SingleAuthorTextView);
        DescriptionTV = findViewById(R.id.SingleDescriptionTextView);
        SourceTV = findViewById(R.id.SingleSourceTextView);
        LinkTV = findViewById(R.id.SingleLinkTextView);
        imageView = findViewById(R.id.SingleImageView);
        LinkTV.setMovementMethod(LinkMovementMethod.getInstance());
        Button button = findViewById(R.id.SingleBackButton);
        button.bringToFront();
        button.setBackgroundColor(getResources().getColor(R.color.DarkGrey));
        PopulateActivity();
    }

    private void PopulateActivity(){
        Picasso.get().load(ImageUrl).into(imageView);
        TitleTV.setText(Title);
        AuthorTv.setText(Author);
        DescriptionTV.setText(Description);
        SourceTV.setText(Name);
        LinkTV.setText(Link);
    }

    public void BackButtonCliked(View view) {
        finish();
    }
}