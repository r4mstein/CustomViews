package com.example.r4mst.customviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.r4mst.customviews.views.ProgressView;

public class MainActivity extends AppCompatActivity {

    private ProgressView mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressView = (ProgressView) findViewById(R.id.progress_view);
    }
}
