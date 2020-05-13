package com.sky21.liquor_app.Home;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.sky21.liquor_app.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
    }
}
