package com.sky21.liquor_app.UserAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.sky21.liquor_app.CartActivity;
import com.sky21.liquor_app.Home.SearchActivity;
import com.sky21.liquor_app.R;
import com.sky21.liquor_app.SharedHelper;

public class HistoryActivity extends AppCompatActivity {
    TextView addressTxt;

    ImageView profile,search, cart;
    String store_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();
        profile=findViewById(R.id.myprofileId);
        search=findViewById(R.id.searchView);
        cart=findViewById(R.id.cart);
        addressTxt=findViewById(R.id.addressId);
        addressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent=getIntent();
        store_id=intent.getStringExtra("STRID");
        SharedHelper.putKey(HistoryActivity.this,"strid","strid");


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
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
