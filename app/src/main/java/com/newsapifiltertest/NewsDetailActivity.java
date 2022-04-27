package com.newsapifiltertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewsDetailActivity extends AppCompatActivity {
    private TextView detailedHeading, detailedSubDesc;
    private Button backBtn;
    String title,desc;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");

        detailedHeading = findViewById(R.id.detailed_tv_heading);
        detailedSubDesc = findViewById(R.id.detailed_tv_subDescription);

        detailedHeading.setText(title);
        detailedSubDesc.setText(desc);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
            }
        });
    }
}