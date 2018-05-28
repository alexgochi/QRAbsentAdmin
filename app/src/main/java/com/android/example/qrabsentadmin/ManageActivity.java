package com.android.example.qrabsentadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class ManageActivity extends AppCompatActivity {
    CardView cv_scanner, cv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        final Activity activity = this;

        cv_scanner = (CardView) findViewById(R.id.cv_scan);
        cv_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        cv_show = (CardView) findViewById(R.id.cv_show);
        cv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageActivity.this, ShowActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ManageActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
