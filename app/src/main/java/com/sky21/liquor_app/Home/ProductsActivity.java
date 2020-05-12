package com.sky21.liquor_app.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky21.liquor_app.CartActivity;
import com.sky21.liquor_app.Home.SearchActivity;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.UserAccount.ProfileActivity;

public class ProductsActivity extends AppCompatActivity {
    TextView addressTxt,filter;
    ImageView profile,search, cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().hide();

        addressTxt=findViewById(R.id.addressId);
        profile=findViewById(R.id.myprofileId);
        search=findViewById(R.id.searchView);
        filter=findViewById(R.id.filter);
        cart=findViewById(R.id.cart);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FilterActivity.class);
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

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CartActivity.class);
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
