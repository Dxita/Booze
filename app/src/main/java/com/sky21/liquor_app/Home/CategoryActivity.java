package com.sky21.liquor_app.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky21.liquor_app.Home.BrandActivity;
import com.sky21.liquor_app.Home.SearchActivity;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.UserAccount.ProfileActivity;

public class CategoryActivity extends AppCompatActivity {
    TextView addressTxt;
    LinearLayout l1;
    ImageView profile,search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().hide();
        addressTxt=findViewById(R.id.addressId);
        profile=findViewById(R.id.myprofileId);
        search=findViewById(R.id.searchView);
        l1=findViewById(R.id.l1);

        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), BrandActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
